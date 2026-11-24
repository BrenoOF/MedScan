package com.medscan.backend.Service;

import com.medscan.backend.DTO.Request.UsuarioRequest;
import com.medscan.backend.DTO.Response.NivelAcessoResponse;
import com.medscan.backend.DTO.Response.UsuarioResponse;
import com.medscan.backend.Model.NivelAcesso;
import com.medscan.backend.Model.Usuario;
import com.medscan.backend.Repository.JPA.NivelAcessoRepository;
import com.medscan.backend.Repository.JPA.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;
    private final NivelAcessoRepository nivelAcessoRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Long ID_NIVEL_PACIENTE_PADRAO = 1L;
    private final AuthenticationManager authenticationManager;

    public UsuarioResponse loginUsuario(UsuarioRequest usuarioRequest,
                                        HttpServletRequest httpRequest,
                                        HttpServletResponse httpResponse) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioRequest.email(), usuarioRequest.senha())
        );

        Usuario usuario = usuarioRepository.findUsuarioByEmailEqualsIgnoreCase(usuarioRequest.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        new HttpSessionSecurityContextRepository().saveContext(context, httpRequest, httpResponse);

        return toResponse(usuario);
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                new NivelAcessoResponse(
                        usuario.getNivelAcesso().getId(),
                        usuario.getNivelAcesso().getDescricao()
                )
        );
    }

    private List<UsuarioResponse> toResponseList(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(this::toResponse)
                .toList();
    }

    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return toResponse(usuario);
    }

    public UsuarioResponse cadastrarUsuario(UsuarioRequest usuarioRequest) {

        if (usuarioRequest.nome() == null || usuarioRequest.nome().isBlank()) {
            throw new RuntimeException("Nome é obrigatório");
        }

        if (usuarioRequest.email() == null || usuarioRequest.email().isBlank()) {
            throw new RuntimeException("Email é obrigatório");
        }

        if (usuarioRequest.senha() == null || usuarioRequest.senha().isBlank()) {
            throw new RuntimeException("Senha é obrigatória");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(usuarioRequest.nome());
        usuario.setEmail(usuarioRequest.email());

        if (usuarioRequest.telefone() == null || usuarioRequest.telefone().isBlank()) {
            usuario.setTelefone("+55 (00)00000-0000");
        } else {
            usuario.setTelefone("+55 " + usuarioRequest.telefone());
        }

        usuario.setSenha(passwordEncoder.encode(usuarioRequest.senha()));

        NivelAcesso nivelPaciente = nivelAcessoRepository.findById(ID_NIVEL_PACIENTE_PADRAO)
                .orElseThrow(() -> new RuntimeException("Nível padrão de paciente não configurado"));

        usuario.setNivelAcesso(nivelPaciente);

        Usuario salvo = usuarioRepository.save(usuario);

        return toResponse(salvo);
    }

    @PreAuthorize("hasAuthority('GERENCIARUSUARIO')")
    public UsuarioResponse criarUsuario(UsuarioRequest usuarioRequest) {

        if (usuarioRequest.nome() == null || usuarioRequest.nome().isBlank()) {
            throw new RuntimeException("Nome é obrigatório");
        }

        if (usuarioRequest.email() == null || usuarioRequest.email().isBlank()) {
            throw new RuntimeException("Email é obrigatório");
        }

        if (usuarioRequest.senha() == null || usuarioRequest.senha().isBlank()) {
            throw new RuntimeException("Senha é obrigatória");
        }

        if (usuarioRequest.nivelAcessoId() == null) {
            throw new RuntimeException("Nível de acesso é obrigatório");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(usuarioRequest.nome());
        usuario.setEmail(usuarioRequest.email());

        if (usuarioRequest.telefone() == null || usuarioRequest.telefone().isBlank()) {
            usuario.setTelefone("+55 (00)00000-0000");
        } else {
            usuario.setTelefone("+55 " + usuarioRequest.telefone());
        }

        usuario.setSenha(passwordEncoder.encode(usuarioRequest.senha()));

        NivelAcesso nivel = nivelAcessoRepository.findById(usuarioRequest.nivelAcessoId())
                .orElseThrow(() -> new RuntimeException("Nível de acesso não encontrado"));

        usuario.setNivelAcesso(nivel);

        Usuario salvo = usuarioRepository.save(usuario);

        return toResponse(salvo);
    }

    @PreAuthorize("hasAuthority('GERENCIARUSUARIO')")
    public List<UsuarioResponse> listar() {
        return toResponseList(usuarioRepository.findAll());
    }

    public List<UsuarioResponse> buscarPorNome(String nome) {
        return toResponseList(
                usuarioRepository.findAllByNomeContainingIgnoreCase(nome)
        );
    }

    public List<UsuarioResponse> buscarPorEmail(String email) {
        return toResponseList(
                usuarioRepository.findAllByEmailContainingIgnoreCase(email)
        );
    }

    public List<UsuarioResponse> buscarPorNivel(Long idNivel) {
        return toResponseList(
                usuarioRepository.findAllByNivelAcessoId(idNivel)
        );
    }

    @PreAuthorize("hasAuthority('GERENCIARUSUARIO')")
    public void deletarUsuario(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuarioRepository.delete(usuario);
    }

    public void logoutUsuario(HttpSession session) {

        if (session != null) {
            session.invalidate();
        }
    }

    @PreAuthorize("hasAuthority('GERENCIARUSUARIO')")
    public UsuarioResponse alterarUsuario(UsuarioRequest request) {

        Usuario usuario = usuarioRepository.findById(request.id())
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setTelefone(request.telefone());

        if (request.senha() != null && !request.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(request.senha()));
        }

        usuarioRepository.save(usuario);

        return toResponse(usuario);
    }
}