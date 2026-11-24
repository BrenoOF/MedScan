package com.medscan.backend.Service;

import com.medscan.backend.DTO.Request.BuscaAtivaRequest;
import com.medscan.backend.DTO.Response.BuscaAtivaResponse;
import com.medscan.backend.Model.BuscaAtiva;
import com.medscan.backend.Repository.JPA.BuscaAtivaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscaAtivaService {

    private final BuscaAtivaRepository buscaAtivaRepository;

    @PreAuthorize("isAuthenticated()")
    public List<BuscaAtivaResponse> listar() {
        return toResponseList(buscaAtivaRepository.findAll());
    }

    @PreAuthorize("isAuthenticated()")
    public BuscaAtivaResponse buscarPorId(Long id) {
        BuscaAtiva buscaAtiva = buscaAtivaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Busca ativa não encontrada"));
        return toResponse(buscaAtiva);
    }

    @PreAuthorize("hasAuthority('CRIARBUSCAATIVA')")
    public BuscaAtivaResponse criar(BuscaAtivaRequest request) {
        BuscaAtiva buscaAtiva = new BuscaAtiva();
        BuscaAtiva salvo = buscaAtivaRepository.save(buscaAtiva);
        return toResponse(salvo);
    }

    @PreAuthorize("hasAuthority('DELETARBUSCAATIVA')")
    public void deletar(Long id) {
        BuscaAtiva buscaAtiva = buscaAtivaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Busca ativa não encontrada"));
        buscaAtivaRepository.delete(buscaAtiva);
    }

    private BuscaAtivaResponse toResponse(BuscaAtiva b) {
        return new BuscaAtivaResponse(
                b.getId(),
                null,
                b.getDescricao()
        );
    }

    private List<BuscaAtivaResponse> toResponseList(List<BuscaAtiva> list) {
        return list.stream().map(this::toResponse).toList();
    }
}