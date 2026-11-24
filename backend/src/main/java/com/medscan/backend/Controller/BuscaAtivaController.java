package com.medscan.backend.Controller;

import com.medscan.backend.DTO.Request.BuscaAtivaRequest;
import com.medscan.backend.DTO.Response.BuscaAtivaResponse;
import com.medscan.backend.Service.BuscaAtivaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buscaAtiva")
@RequiredArgsConstructor
public class BuscaAtivaController {

    private final BuscaAtivaService buscaAtivaService;

    @PostMapping("/buscaAtivaList")
    public ResponseEntity<Map<String, Object>> listarBuscaAtiva(@RequestBody(required = false) Map<String, Long> filtros) {

        List<BuscaAtivaResponse> lista;

        if (filtros != null && filtros.get("id") != null) {
            try {
                lista = List.of(buscaAtivaService.buscarPorId(filtros.get("id")));
            } catch (RuntimeException e) {
                lista = List.of();
            }
        } else {
            lista = buscaAtivaService.listar();
        }

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("buscaAtivas", lista);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/buscaAtivaNew")
    public ResponseEntity<?> criarBuscaAtiva(@RequestBody BuscaAtivaRequest request) {
        BuscaAtivaResponse novaBuscaAtiva = buscaAtivaService.criar(request);
        return ResponseEntity.ok(novaBuscaAtiva);
    }

    @PostMapping("/buscaAtivaDelete")
    public ResponseEntity<?> deletarBuscaAtiva(@RequestBody Map<String, Long> body) {
        Long id = body.get("id");

        if (id == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "ID inválido"));
        }

        buscaAtivaService.deletar(id);

        return ResponseEntity.ok(Map.of("message", "Busca ativa excluída com sucesso"));
    }
}