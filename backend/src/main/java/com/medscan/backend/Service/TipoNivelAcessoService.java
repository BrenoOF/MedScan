package com.medscan.backend.Service;

import com.medscan.backend.DTO.Response.TipoNivelAcessoResponse;
import com.medscan.backend.Model.TipoNivelAcesso;
import com.medscan.backend.Repository.JPA.TipoNivelAcessoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoNivelAcessoService {

    private final TipoNivelAcessoRepository tipoNivelAcessoRepository;

    @PreAuthorize("isAuthenticated()")
    public List<TipoNivelAcessoResponse> listarPorNivel(Long nivelId) {
        return toResponseList(tipoNivelAcessoRepository.findByNivelAcessoId(nivelId));
    }

    private TipoNivelAcessoResponse toResponse(TipoNivelAcesso t) {
        return new TipoNivelAcessoResponse(t.getId(), t.getNivelAcesso().getId(), t.getCategoriaNivelAcesso().getId());
    }

    private List<TipoNivelAcessoResponse> toResponseList(List<TipoNivelAcesso> list) {
        return list.stream().map(this::toResponse).toList();
    }
}