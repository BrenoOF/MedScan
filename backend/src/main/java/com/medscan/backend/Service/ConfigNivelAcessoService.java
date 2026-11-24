package com.medscan.backend.Service;

import com.medscan.backend.DTO.Request.ConfigNivelAcessoRequest;
import com.medscan.backend.DTO.Response.ConfigNivelAcessoResponse;
import com.medscan.backend.Model.CategoriaNivelAcesso;
import com.medscan.backend.Model.ConfigNivelAcesso;
import com.medscan.backend.Repository.JPA.CategoriaNivelAcessoRepository;
import com.medscan.backend.Repository.JPA.ConfigNivelAcessoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfigNivelAcessoService {

    private final ConfigNivelAcessoRepository configNivelAcessoRepository;
    private final CategoriaNivelAcessoRepository categoriaNivelAcessoRepository;

    @PreAuthorize("isAuthenticated()")
    public List<ConfigNivelAcessoResponse> listar() {
        return toResponseList(configNivelAcessoRepository.findAll());
    }

    @PreAuthorize("hasAuthority('GERENCIARCONFIGACESSO')")
    public ConfigNivelAcessoResponse criar(ConfigNivelAcessoRequest request) {
        CategoriaNivelAcesso categoria = categoriaNivelAcessoRepository.findById(request.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        ConfigNivelAcesso config = new ConfigNivelAcesso();
        config.setDescricao(request.descricao());
        config.setStatus(request.status());
        config.setCategoriaNivelAcesso(categoria);

        return toResponse(configNivelAcessoRepository.save(config));
    }

    @PreAuthorize("hasAuthority('GERENCIARCONFIGACESSO')")
    public ConfigNivelAcessoResponse alterarStatus(Long id, Boolean status) {
        ConfigNivelAcesso config = configNivelAcessoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Config não encontrada"));
        config.setStatus(status);
        return toResponse(configNivelAcessoRepository.save(config));
    }

    @PreAuthorize("hasAuthority('GERENCIARCONFIGACESSO')")
    public void deletar(Long id) {
        ConfigNivelAcesso config = configNivelAcessoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Config não encontrada"));
        configNivelAcessoRepository.delete(config);
    }

    private ConfigNivelAcessoResponse toResponse(ConfigNivelAcesso c) {
        return new ConfigNivelAcessoResponse(c.getId(), c.getDescricao(), c.getCategoriaNivelAcesso().getId(), c.getStatus());
    }

    private List<ConfigNivelAcessoResponse> toResponseList(List<ConfigNivelAcesso> list) {
        return list.stream().map(this::toResponse).toList();
    }
}