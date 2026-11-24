package com.medscan.backend.Service;

import com.medscan.backend.DTO.Response.TipoAgendamentoResponse;
import com.medscan.backend.Repository.JPA.TipoServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoServicoService {

    private final TipoServicoRepository tipoServicoRepository;

    @PreAuthorize("isAuthenticated()")
    public List<TipoAgendamentoResponse> listarPorUnidade(Long cnes) {
        return tipoServicoRepository.buscarPorCnes(cnes);
    }
}