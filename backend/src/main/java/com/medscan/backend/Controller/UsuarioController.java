package com.medscan.backend.Controller;

import com.medscan.backend.DTO.Request.UsuarioRequest;
import com.medscan.backend.DTO.Response.UsuarioResponse;
import com.medscan.backend.Service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/usuarioCadastro")
    public ResponseEntity<Map<String, Object>> cadastrarUsuario(@RequestBody UsuarioRequest request) {
        UsuarioResponse usuario = usuarioService.cadastrarUsuario(request);
        return ResponseEntity.ok(Map.of("message", "Cadastro realizado com sucesso", "usuario", usuario));
    }

    @PostMapping("/usuarioNew")
    public ResponseEntity<Map<String, Object>> criarUsuario(@RequestBody UsuarioRequest request) {
        UsuarioResponse usuario = usuarioService.criarUsuario(request);
        return ResponseEntity.ok(Map.of("message", "Usuário cadastrado com sucesso", "usuario", usuario));
    }

    @PostMapping("/usuarioLogin")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UsuarioRequest request,
                                                     HttpServletRequest httpRequest,
                                                     HttpServletResponse httpResponse) {
        UsuarioResponse usuario = usuarioService.loginUsuario(request, httpRequest, httpResponse);
        return ResponseEntity.ok(Map.of("message", "Login realizado com sucesso", "usuario", usuario));
    }

    @GetMapping("/usuarioSession")
    public ResponseEntity<Map<String, Object>> usuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(401).body(Map.of("message", "Nenhum usuário logado"));
        }

        UsuarioResponse usuario = usuarioService.buscarPorEmail(auth.getName())
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return ResponseEntity.ok(Map.of("usuario", usuario));
    }

    @PostMapping("/usuarioList")
    public ResponseEntity<Map<String, Object>> listarUsuarios(
            @RequestBody(required = false) Map<String, Object> filtros) {

        if (filtros == null) {
            return ResponseEntity.badRequest().build();
        }

        String filtro = (String) filtros.get("filtro");
        String valor = (String) filtros.get("valor");

        if (filtro == null) {
            return ResponseEntity.badRequest().build();
        }

        List<UsuarioResponse> lista;

        switch (filtro.toLowerCase()) {

            case "listar todos":
                lista = usuarioService.listar();
                break;

            case "nome":
                if (valor == null || valor.isBlank()) {
                    return ResponseEntity.badRequest().build();
                }
                lista = usuarioService.buscarPorNome(valor);
                break;

            case "email":
                if (valor == null || valor.isBlank()) {
                    return ResponseEntity.badRequest().build();
                }
                lista = usuarioService.buscarPorEmail(valor);
                break;

            case "nivel":
                if (valor == null || valor.isBlank()) {
                    return ResponseEntity.badRequest().build();
                }

                Long idNivel = Long.parseLong(valor);
                lista = usuarioService.buscarPorNivel(idNivel);
                break;

            default:
                return ResponseEntity.badRequest().build();
        }

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                Map.of("usuarios", lista)
        );
    }

    @PostMapping("/usuarioDelete")
    public ResponseEntity<Map<String, Object>> deletarUsuario(
            @RequestBody Map<String, Long> body) {

        try {
            Long id = body.get("id");
            if (id == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "message",
                                "ID não informado"
                        ));
            }
            usuarioService.deletarUsuario(id);
            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Usuário removido com sucesso"
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(404)
                    .body(Map.of(
                            "message",
                            e.getMessage()
                    ));
        }
    }

    @PostMapping("/usuarioLogout")
    public ResponseEntity<Map<String, Object>> logoutUsuario(
            HttpSession session) {
        try {
            usuarioService.logoutUsuario(session);
            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Logout realizado com sucesso"
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(
                            Map.of(
                                    "message",
                                    "Erro ao realizar logout"
                            )
                    );
        }
    }


}
