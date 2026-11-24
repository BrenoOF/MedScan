package com.medscan.backend;

import com.medscan.backend.Configuration.UsuarioDetailsService;
import com.medscan.backend.DTO.Request.UsuarioRequest;
import com.medscan.backend.Model.Funcao;
import com.medscan.backend.Model.NivelAcesso;
import com.medscan.backend.Model.Usuario;
import com.medscan.backend.Repository.JPA.UsuarioRepository;
import com.medscan.backend.Service.UsuarioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsuarioOptionalTest {

    private static final String EMAIL = "usuario@example.com";
    private final UsuarioRepository repository = mock(UsuarioRepository.class);
    private final UsuarioDetailsService detailsService = new UsuarioDetailsService(repository);
    private final AuthenticationManager authenticationManager = request ->
            UsernamePasswordAuthenticationToken.authenticated(request.getPrincipal(), null, List.of());
    private final UsuarioService usuarioService = new UsuarioService(
            repository, null, null, authenticationManager);
    private final UsuarioRequest credenciais = new UsuarioRequest(null, null, EMAIL, "senha", null, null);

    @AfterEach
    void limparContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void carregaCredenciaisDoUsuarioEncontrado() {
        when(repository.findUsuarioByEmailEqualsIgnoreCase(EMAIL)).thenReturn(Optional.of(usuario()));

        assertEquals(EMAIL, detailsService.loadUserByUsername(EMAIL).getUsername());
    }

    @Test
    void rejeitaCredenciaisQuandoUsuarioNaoExiste() {
        when(repository.findUsuarioByEmailEqualsIgnoreCase(EMAIL)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> detailsService.loadUserByUsername(EMAIL));
    }

    @Test
    void retornaDadosDoUsuarioEncontradoNoLogin() {
        when(repository.findUsuarioByEmailEqualsIgnoreCase(EMAIL)).thenReturn(Optional.of(usuario()));

        var resposta = usuarioService.loginUsuario(
                credenciais, new MockHttpServletRequest(), new MockHttpServletResponse());

        assertEquals(EMAIL, resposta.email());
    }

    @Test
    void rejeitaLoginQuandoConsultaNaoEncontraUsuarioAposAutenticacao() {
        when(repository.findUsuarioByEmailEqualsIgnoreCase(EMAIL)).thenReturn(Optional.empty());
        var request = new MockHttpServletRequest();

        assertThrows(BadCredentialsException.class,
                () -> usuarioService.loginUsuario(credenciais, request, new MockHttpServletResponse()));
        assertNull(request.getSession(false));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    private Usuario usuario() {
        var funcao = new Funcao();
        funcao.setNome("Paciente");
        var nivel = new NivelAcesso();
        nivel.setFuncao(funcao);
        var usuario = new Usuario();
        usuario.setEmail(EMAIL);
        usuario.setSenha("hash");
        usuario.setNivelAcesso(nivel);
        return usuario;
    }
}
