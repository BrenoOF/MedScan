package com.medscan.backend.Service;

import com.medscan.backend.DTO.Request.CategoriaNivelAcessoRequest;
import com.medscan.backend.DTO.Response.CategoriaNivelAcessoResponse;
import com.medscan.backend.Model.CategoriaNivelAcesso;
import com.medscan.backend.Repository.JPA.CategoriaNivelAcessoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaNivelAcessoService {

    private final CategoriaNivelAcessoRepository categoriaNivelAcessoRepository;

    @PreAuthorize("isAuthenticated()")
    public List<CategoriaNivelAcessoResponse> listar() {
        return toResponseList(categoriaNivelAcessoRepository.findAll());
    }

    @PreAuthorize("hasAuthority('GERENCIARCATEGORIAACESSO')")
    public CategoriaNivelAcessoResponse criar(CategoriaNivelAcessoRequest request) {
        CategoriaNivelAcesso categoria = new CategoriaNivelAcesso();
        categoria.setDescricao(request.descricao());
        return toResponse(categoriaNivelAcessoRepository.save(categoria));
    }

    @PreAuthorize("hasAuthority('GERENCIARCATEGORIAACESSO')")
    public void deletar(Long id) {
        CategoriaNivelAcesso categoria = categoriaNivelAcessoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        categoriaNivelAcessoRepository.delete(categoria);
    }

    private CategoriaNivelAcessoResponse toResponse(CategoriaNivelAcesso c) {
        return new CategoriaNivelAcessoResponse(c.getId(), c.getDescricao());
    }

    private List<CategoriaNivelAcessoResponse> toResponseList(List<CategoriaNivelAcesso> list) {
        return list.stream().map(this::toResponse).toList();
    }
}