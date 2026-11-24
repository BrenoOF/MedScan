package com.medscan.backend.Service;

import com.medscan.backend.DTO.Response.EnderecoResponse;
import com.medscan.backend.DTO.Response.UnidadeSaudeResponse;
import com.medscan.backend.Model.UnidadeSaude;
import com.medscan.backend.Repository.JPA.UnidadeSaudeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnidadeSaudeService {

    private final UnidadeSaudeRepository unidadeSaudeRepository;

    @PreAuthorize("isAuthenticated()")
    public UnidadeSaudeResponse buscarPorCnes(Long cnes) {
        UnidadeSaude unidadeSaude = unidadeSaudeRepository.findByCnes(cnes);
        if (unidadeSaude == null) {
            throw new RuntimeException("Unidade de Saúde não encontrada");
        }
        return toResponse(unidadeSaude);
    }

    @PreAuthorize("isAuthenticated()")
    public List<UnidadeSaudeResponse> listarTodos() {
        return toResponseList(unidadeSaudeRepository.findAll());
    }

    @PreAuthorize("isAuthenticated()")
    public List<UnidadeSaudeResponse> buscarPorNome(String nome) {
        return toResponseList(unidadeSaudeRepository.findAllByNomeContainingIgnoreCase(nome));
    }

    private UnidadeSaudeResponse toResponse(UnidadeSaude u) {
        return new UnidadeSaudeResponse(
                u.getCnes(),
                u.getNome(),
                new EnderecoResponse(
                        u.getEndereco().getId(),
                        u.getEndereco().getLogradouro(),
                        u.getEndereco().getNumero(),
                        u.getEndereco().getBairro(),
                        u.getEndereco().getCidade(),
                        u.getEndereco().getUf(),
                        u.getEndereco().getCep(),
                        u.getEndereco().getComplemento()
                )
        );
    }

    private List<UnidadeSaudeResponse> toResponseList(List<UnidadeSaude> unidades) {
        return unidades.stream().map(this::toResponse).toList();
    }
}