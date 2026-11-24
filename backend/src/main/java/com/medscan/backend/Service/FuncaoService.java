package com.medscan.backend.Service;

import com.medscan.backend.DTO.Request.FuncaoRequest;
import com.medscan.backend.DTO.Response.FuncaoResponse;
import com.medscan.backend.Model.Funcao;
import com.medscan.backend.Repository.JPA.FuncaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FuncaoService {

    private final FuncaoRepository funcaoRepository;

    @PreAuthorize("isAuthenticated()")
    public List<FuncaoResponse> listar() {
        return toResponseList(funcaoRepository.findAll());
    }

    @PreAuthorize("isAuthenticated()")
    public FuncaoResponse listarPorIdResponse(Long id) {
        return toResponse(funcaoRepository.findFuncaoById(id));
    }

    public Funcao listarPorId(Long id) {
        return funcaoRepository.findFuncaoById(id);
    }

    @PreAuthorize("hasAuthority('GERENCIARFUNCAO')")
    public FuncaoResponse criar(FuncaoRequest request) {
        Funcao funcao = new Funcao();
        funcao.setNome(request.nome());
        return toResponse(funcaoRepository.save(funcao));
    }

    private FuncaoResponse toResponse(Funcao funcao) {
        return new FuncaoResponse(funcao.getId(), funcao.getNome());
    }

    private List<FuncaoResponse> toResponseList(List<Funcao> funcoes) {
        return funcoes.stream().map(this::toResponse).toList();
    }
}