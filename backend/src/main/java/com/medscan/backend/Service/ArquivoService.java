package com.medscan.backend.Service;

import com.medscan.backend.Model.Arquivo;
import com.medscan.backend.Model.Paciente;
import com.medscan.backend.Model.Usuario;
import com.medscan.backend.Repository.JPA.ArquivoRepository;
import com.medscan.backend.Repository.JPA.PacienteRepository;
import com.medscan.backend.Repository.JPA.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArquivoService {

    private final ArquivoRepository arquivoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository; // Injetado via Lombok para segurança

    @Value("${medscan.upload.dir}")
    private String uploadDir;

    @Transactional
    public Arquivo uploadArquivo(
            MultipartFile file,
            Arquivo.TipoArquivo tipoArquivo,
            Arquivo.TipoEntidade tipoEntidade,
            Long idEntidade,
            Long usuarioId,
            String descricao
    ) throws IOException {
        // Validações
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio não pode ser enviado");
        }

        String nomeOriginal = file.getOriginalFilename();
        String extensao = nomeOriginal != null && nomeOriginal.contains(".")
                ? nomeOriginal.substring(nomeOriginal.lastIndexOf("."))
                : "";

        // Gerar nome único para o arquivo
        String nomeArquivo = UUID.randomUUID().toString() + extensao;

        // Criar diretório se não existir
        Path dirPath = Paths.get(uploadDir, tipoEntidade.name().toLowerCase());
        Files.createDirectories(dirPath);

        // Salvar arquivo no sistema de arquivos
        Path caminhoCompleto = dirPath.resolve(nomeArquivo);
        Files.copy(file.getInputStream(), caminhoCompleto, StandardCopyOption.REPLACE_EXISTING);

        // Buscar usuário
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Criar registro no banco de dados
        Arquivo arquivo = new Arquivo();
        arquivo.setNomeOriginal(nomeOriginal);
        arquivo.setNomeArquivo(nomeArquivo);
        arquivo.setCaminhoArquivo(caminhoCompleto.toString());
        arquivo.setTipoArquivo(tipoArquivo);
        arquivo.setMimeType(file.getContentType());
        arquivo.setTamanhoBytes(file.getSize());
        arquivo.setTipoEntidade(tipoEntidade);
        arquivo.setIdEntidade(idEntidade);
        arquivo.setDataUpload(LocalDateTime.now());
        arquivo.setUploadPorUsuario(usuario);
        arquivo.setDescricao(descricao);

        return arquivoRepository.save(arquivo);
    }

    public Resource downloadArquivo(Long arquivoId) throws IOException {
        Arquivo arquivo = arquivoRepository.findById(arquivoId)
                .orElseThrow(() -> new IllegalArgumentException("Arquivo não encontrado"));

        // 1. Obter usuário autenticado no contexto do Spring Security
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Proteção contra chamadas não autenticadas
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("Usuário não autenticado.");
        }

        String loginUsuario = auth.getName();

        Usuario usuarioLogado = usuarioRepository.findUsuarioByEmailEqualsIgnoreCase(loginUsuario) // Ajustado para findByEmail com base no seu UsuarioDetailsService
                .orElseThrow(() -> new RuntimeException("Usuário inválido ou não encontrado no banco."));

        // 2. Regra de Negócio: Verificar permissão de acesso
        boolean temPermissao = validarPermissaoDownload(arquivo, usuarioLogado);

        if (!temPermissao) {
            throw new AccessDeniedException("Você não tem permissão para acessar este documento médico.");
        }

        Path caminhoArquivo = Paths.get(arquivo.getCaminhoArquivo());
        Resource resource = new UrlResource(caminhoArquivo.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("Arquivo não encontrado ou não pode ser lido: " + arquivo.getNomeArquivo());
        }
    }

    private boolean validarPermissaoDownload(Arquivo arquivo, Usuario usuarioLogado) {
        String nomeFuncao = usuarioLogado.getNivelAcesso().getFuncao().getNome();

        // Supervisor tem acesso irrestrito
        if (nomeFuncao.equalsIgnoreCase("SUPERVISOR")) {
            return true;
        }

        // Paciente só pode ver arquivos onde o tipoEntidade seja PACIENTE e o ID seja o dele mesmo
        if (nomeFuncao.equalsIgnoreCase("PACIENTE")) {
            Paciente paciente = pacienteRepository.findByUsuarioId(usuarioLogado.getId())
                    .orElseThrow(() -> new RuntimeException("Perfil de paciente não encontrado para este usuário."));

            return arquivo.getTipoEntidade() == Arquivo.TipoEntidade.PACIENTE
                    && arquivo.getIdEntidade().equals(paciente.getId());
        }

        // Atendente tem acesso liberado provisoriamente
        if (nomeFuncao.equalsIgnoreCase("ATENDENTE")) {
            return true;
        }

        return false;
    }

    public Arquivo obterArquivoPorId(Long id) {
        return arquivoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Arquivo não encontrado"));
    }

    public List<Arquivo> listarArquivosPorEntidade(Arquivo.TipoEntidade tipoEntidade, Long idEntidade) {
        return arquivoRepository.findByTipoEntidadeAndIdEntidade(tipoEntidade, idEntidade);
    }

    public List<Arquivo> listarArquivosPorTipoEEntidade(
            Arquivo.TipoArquivo tipoArquivo,
            Arquivo.TipoEntidade tipoEntidade,
            Long idEntidade
    ) {
        return arquivoRepository.findByTipoArquivoAndTipoEntidadeAndIdEntidade(tipoArquivo, tipoEntidade, idEntidade);
    }

    @Transactional
    public void excluirArquivo(Long arquivoId) throws IOException {
        Arquivo arquivo = arquivoRepository.findById(arquivoId)
                .orElseThrow(() -> new IllegalArgumentException("Arquivo não encontrado"));

        // Excluir arquivo físico
        Path caminhoArquivo = Paths.get(arquivo.getCaminhoArquivo());
        Files.deleteIfExists(caminhoArquivo);

        // Excluir registro do banco
        arquivoRepository.delete(arquivo);
    }

    public Arquivo obterFotoPerfilPaciente(Long pacienteId) {
        List<Arquivo> arquivos = arquivoRepository.findByTipoArquivoAndTipoEntidadeAndIdEntidade(
                Arquivo.TipoArquivo.FOTO_PERFIL,
                Arquivo.TipoEntidade.PACIENTE,
                pacienteId
        );
        return arquivos.isEmpty() ? null : arquivos.get(0);
    }
}