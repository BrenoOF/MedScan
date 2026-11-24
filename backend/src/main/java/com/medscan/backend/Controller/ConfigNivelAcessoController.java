package com.medscan.backend.Controller;

import com.medscan.backend.DTO.Request.ConfigNivelAcessoRequest;
import com.medscan.backend.DTO.Response.ConfigNivelAcessoResponse;
import com.medscan.backend.Service.ConfigNivelAcessoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/configNivelAcesso")
@RequiredArgsConstructor
public class ConfigNivelAcessoController {

    private final ConfigNivelAcessoService configNivelAcessoService;

    @PostMapping("/configNivelAcessoList")
    public ResponseEntity<Map<String, Object>> listar() {
        List<ConfigNivelAcessoResponse> lista = configNivelAcessoService.listar();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("configNivelAcesso", lista);

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/configNivelAcessoNew")
    public ResponseEntity<?> criar(@RequestBody ConfigNivelAcessoRequest request) {
        ConfigNivelAcessoResponse novoConfig = configNivelAcessoService.criar(request);
        return ResponseEntity.ok(novoConfig);
    }

    @PostMapping("/configNivelAcessoStatus")
    public ResponseEntity<?> alterarStatus(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        Boolean status = (Boolean) body.get("status");
        return ResponseEntity.ok(configNivelAcessoService.alterarStatus(id, status));
    }

    @PostMapping("/configNivelAcessoDelete")
    public ResponseEntity<?> deletar(@RequestBody Map<String, Long> body) {
        Long id = body.get("id");

        if (id == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "ID inválido"));
        }

        configNivelAcessoService.deletar(id);
        return ResponseEntity.ok(Map.of("message", "Config excluída com sucesso"));
    }
}