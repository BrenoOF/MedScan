package com.medscan.backend.Service;

import com.medscan.backend.DTO.Request.AtendenteRequest;
import com.medscan.backend.DTO.Response.AtendenteResponse;
import com.medscan.backend.Model.Atendente;
import com.medscan.backend.Model.UnidadeSaude;
import com.medscan.backend.Model.Usuario;
import com.medscan.backend.Repository.JPA.AtendenteRepository;
import com.medscan.backend.Repository.JPA.UnidadeSaudeRepository;
import com.medscan.backend.Repository.JPA.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AtendenteService {

    private final AtendenteRepository atendenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final UnidadeSaudeRepository unidadeSaudeRepository;

    @Transactional
    public AtendenteResponse criar(AtendenteRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        UnidadeSaude unidadeSaude = unidadeSaudeRepository.findById(request.unidadeSaudeId())
                .orElseThrow(() -> new IllegalArgumentException("Unidade de Saúde não encontrada."));

        Atendente atendente = new Atendente();
        atendente.setUsuario(usuario);
        atendente.setUnidadeSaude(unidadeSaude);

        atendente = atendenteRepository.save(atendente);

        return toResponse(atendente);
    }

    private AtendenteResponse toResponse(Atendente atendente) {
        Long usuarioId = atendente.getUsuario() != null ? atendente.getUsuario().getId() : null;
        Long unidadeSaudeId = atendente.getUnidadeSaude() != null ? atendente.getUnidadeSaude().getCnes() : null;

        return new AtendenteResponse(
                atendente.getId(),
                usuarioId,
                unidadeSaudeId,
                Collections.emptyList()
        );
    }
}