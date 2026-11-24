package com.medscan.backend.Controller;

import com.medscan.backend.DTO.Request.FuncaoRequest;
import com.medscan.backend.DTO.Response.FuncaoResponse;
import com.medscan.backend.Service.FuncaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/funcao")
@RequiredArgsConstructor
public class FuncaoController {

    private final FuncaoService funcaoService;

    @PostMapping("/funcaoList")
    public ResponseEntity<Map<String, Object>> listar() {
        List<FuncaoResponse> lista = funcaoService.listar();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("funcao", lista);

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/funcaoNew")
    public ResponseEntity<?> criar(@RequestBody FuncaoRequest request) {
        FuncaoResponse nova = funcaoService.criar(request);
        return ResponseEntity.ok(nova);
    }
}