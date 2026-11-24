package com.medscan.backend.Controller;

import com.medscan.backend.DTO.Response.TipoNivelAcessoResponse;
import com.medscan.backend.Service.TipoNivelAcessoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tipoNivelAcesso")
@RequiredArgsConstructor
public class TipoNivelAcessoController {

    private final TipoNivelAcessoService tipoNivelAcessoService;

    @PostMapping("/tipoNivelAcessoId")
    public ResponseEntity<Map<String, Object>> listar(@RequestBody Map<String, Long> req) {

        Long nivelAcessoId = req.get("nivelAcessoId");
        if (nivelAcessoId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "nivelAcessoId é obrigatório"));
        }

        List<TipoNivelAcessoResponse> lista = tipoNivelAcessoService.listarPorNivel(nivelAcessoId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(Map.of("tipoNivelAcesso", lista));
    }
}

