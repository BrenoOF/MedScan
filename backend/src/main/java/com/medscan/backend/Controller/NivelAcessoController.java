package com.medscan.backend.Controller;

import com.medscan.backend.DTO.Request.NivelAcessoRequest;
import com.medscan.backend.DTO.Response.NivelAcessoResponse;
import com.medscan.backend.Service.NivelAcessoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nivelAcesso")
@RequiredArgsConstructor
public class NivelAcessoController {

    private final NivelAcessoService nivelAcessoService;

    @PostMapping("/nivelAcessoList")
    public ResponseEntity<Map<String, Object>> listarNivel(@RequestBody(required = false) Map<String, Object> filtros) {

        List<NivelAcessoResponse> lista;

        if (filtros != null && filtros.containsKey("id") && filtros.get("id") != null) {
            Long id = Long.valueOf(filtros.get("id").toString());
            try {
                lista = List.of(nivelAcessoService.buscarPorId(id));
            } catch (RuntimeException e) {
                lista = List.of();
            }

        } else if (filtros != null && filtros.containsKey("descricao") && filtros.get("descricao") != null) {
            String descricao = (String) filtros.get("descricao");
            lista = nivelAcessoService.buscarPorNome(descricao);

        } else {
            lista = nivelAcessoService.listar();
        }

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("niveisAcesso", lista);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/nivelAcessoNew")
    public ResponseEntity<?> criarNivel(@RequestBody NivelAcessoRequest request) {
        NivelAcessoResponse novoNivel = nivelAcessoService.criarNivelAcesso(request);
        return ResponseEntity.ok(novoNivel);
    }

    @PostMapping("/nivelAcessoDelete")
    public ResponseEntity<?> deletarNivelAcesso(@RequestBody Map<String, Long> body) {
        Long id = body.get("id");

        if (id == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "ID inválido"));
        }

        nivelAcessoService.deletarNivelAcesso(id);

        return ResponseEntity.ok(Map.of("message", "Nível de acesso excluído com sucesso"));
    }


}