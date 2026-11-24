package com.medscan.backend.Controller;

import com.medscan.backend.DTO.Response.UnidadeSaudeResponse;
import com.medscan.backend.Service.UnidadeSaudeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/unidadeSaude")
@RequiredArgsConstructor
public class UnidadeSaudeController {

    private final UnidadeSaudeService unidadeSaudeService;

    @PostMapping("/unidadeSaudeList")
    public ResponseEntity<?> listarTodas() {
        List<UnidadeSaudeResponse> lista = unidadeSaudeService.listarTodos();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("unidadeSaude", lista);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/buscarPorNome")
    public ResponseEntity<?> buscarPorNome(@RequestBody Map<String, String> body) {
        String nome = body.get("nome");
        List<UnidadeSaudeResponse> response = unidadeSaudeService.buscarPorNome(nome);

        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("unidadeSaude", response);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/buscarPorCnes")
    public ResponseEntity<UnidadeSaudeResponse> buscarPorCnes(@RequestBody Map<String, Long> body) {
        UnidadeSaudeResponse response = unidadeSaudeService.buscarPorCnes(body.get("cnes"));
        return ResponseEntity.ok(response);
    }
}