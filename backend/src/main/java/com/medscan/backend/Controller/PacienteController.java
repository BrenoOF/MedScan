
package com.medscan.backend.Controller;

import com.medscan.backend.DTO.Request.PacienteRequest;
import com.medscan.backend.DTO.Response.PacienteResponse;
import com.medscan.backend.Service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/paciente")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping("/pacienteCadastro")
    public ResponseEntity<Map<String, Object>> cadastrarPaciente(
            @RequestBody PacienteRequest request) {

        try {
            PacienteResponse paciente = pacienteService.criar(request);

            return ResponseEntity.ok(Map.of(
                    "message", "Paciente cadastrado com sucesso",
                    "paciente", paciente
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/pacientePerfil")
    public ResponseEntity<Map<String, Object>> buscarPerfilLogado() {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null ||
                !auth.isAuthenticated() ||
                auth instanceof AnonymousAuthenticationToken) {

            return ResponseEntity.status(401).body(Map.of(
                    "message", "Nenhum usuário logado"
            ));
        }

        try {
            PacienteResponse paciente = pacienteService.buscarPerfilLogado();

            return ResponseEntity.ok(Map.of(
                    "message", "Perfil encontrado com sucesso",
                    "paciente", paciente
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "message", e.getMessage()
            ));
        }
    }
}
