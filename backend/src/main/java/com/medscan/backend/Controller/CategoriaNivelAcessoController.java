package com.medscan.backend.Controller;

import com.medscan.backend.DTO.Request.CategoriaNivelAcessoRequest;
import com.medscan.backend.DTO.Response.CategoriaNivelAcessoResponse;
import com.medscan.backend.Service.CategoriaNivelAcessoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categoriaNivelAcesso")
@RequiredArgsConstructor
public class CategoriaNivelAcessoController {

    private final CategoriaNivelAcessoService categoriaNivelAcessoService;

    @PostMapping("/categoriaNivelAcessoList")
    public ResponseEntity<Map<String, Object>> listar() {
        List<CategoriaNivelAcessoResponse> lista = categoriaNivelAcessoService.listar();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("categoriaNivelAcesso", lista);

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/categoriaNivelAcessoNew")
    public ResponseEntity<?> criar(@RequestBody CategoriaNivelAcessoRequest request) {
        CategoriaNivelAcessoResponse nova = categoriaNivelAcessoService.criar(request);
        return ResponseEntity.ok(nova);
    }

    @PostMapping("/categoriaNivelAcessoDelete")
    public ResponseEntity<?> deletar(@RequestBody Map<String, Long> body) {
        Long id = body.get("id");

        if (id == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "ID inválido"));
        }

        categoriaNivelAcessoService.deletar(id);
        return ResponseEntity.ok(Map.of("message", "Categoria excluída com sucesso"));
    }
}