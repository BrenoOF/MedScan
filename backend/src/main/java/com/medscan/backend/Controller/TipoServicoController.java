package com.medscan.backend.Controller;

import com.medscan.backend.DTO.Response.TipoAgendamentoResponse;
import com.medscan.backend.Service.TipoServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tipoServico")
@RequiredArgsConstructor
public class TipoServicoController {

    private final TipoServicoService tipoServicoService;

    @PostMapping("/listarPorUnidade")
    public ResponseEntity<?> listar(@RequestBody Map<String, Long> req) {
        Long cnes = req.get("unidadeId");
        List<TipoAgendamentoResponse> lista = tipoServicoService.listarPorUnidade(cnes);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("tipoServico", lista);
        return ResponseEntity.ok(resp);
    }
}