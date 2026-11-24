package com.medscan.backend.Service;

import com.medscan.backend.DTO.Request.NivelAcessoRequest;
import com.medscan.backend.DTO.Response.NivelAcessoResponse;
import com.medscan.backend.Model.Funcao;
import com.medscan.backend.Model.NivelAcesso;
import com.medscan.backend.Repository.JPA.NivelAcessoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NivelAcessoService {

    private final NivelAcessoRepository nivelAcessoRepository;
    private final FuncaoService funcaoService;

    private NivelAcessoResponse toResponse(NivelAcesso nivelAcesso) {
        return new NivelAcessoResponse(
                nivelAcesso.getId(),
                nivelAcesso.getDescricao()
        );
    }

    private List<NivelAcessoResponse> toResponseList(List<NivelAcesso> nivelAcessoList) {
        return nivelAcessoList.stream()
                .map(this::toResponse)
                .toList();
    }

    @PreAuthorize("isAuthenticated()")
    public List<NivelAcessoResponse> listar() {
        return nivelAcessoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @PreAuthorize("isAuthenticated()")
    public NivelAcessoResponse buscarPorId(Long id) {
        NivelAcesso nivel = nivelAcessoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nível de Acesso não encontrado"));
        return toResponse(nivel);
    }

    @PreAuthorize("isAuthenticated()")
    public List<NivelAcessoResponse> buscarPorNome(String nome) {
        return toResponseList(nivelAcessoRepository.findByDescricaoContainingIgnoreCase(nome));
    }

    @PreAuthorize("hasAuthority('GERENCIARNIVELACESSO')")
    public NivelAcessoResponse criarNivelAcesso(NivelAcessoRequest request) {
        if (request.descricao() == null || request.descricao().trim().isEmpty()) {
            throw new RuntimeException("A descrição é obrigatória.");
        }

        Funcao funcao;

        try {
            funcao = funcaoService.listarPorId(request.funcaoId());
        } catch (RuntimeException e) {
            throw new RuntimeException("Função não encontrada no banco de dados.");
        }

        NivelAcesso nivel = new NivelAcesso();
        nivel.setDescricao(request.descricao());
        nivel.setFuncao(funcao);

        NivelAcesso salvo = nivelAcessoRepository.save(nivel);

        return toResponse(salvo);
    }

    @PreAuthorize("hasAuthority('GERENCIARNIVELACESSO')")
    public void deletarNivelAcesso(Long id) {
        NivelAcesso nivel = nivelAcessoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nível de acesso não encontrado"));

        nivelAcessoRepository.delete(nivel);
    }
}