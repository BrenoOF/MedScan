package com.medscan.backend.Configuration;

import com.medscan.backend.Model.*;
import com.medscan.backend.Repository.JPA.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) {

        Usuario usuario = usuarioRepository.findUsuarioByEmailEqualsIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas"));

        Set<GrantedAuthority> authorities = new HashSet<>();
        NivelAcesso nivelAcesso = usuario.getNivelAcesso();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + normalizar(nivelAcesso.getFuncao().getNome())));

        for (TipoNivelAcesso tipo : nivelAcesso.getTipoNivelAcessoList()) {
            CategoriaNivelAcesso categoria = tipo.getCategoriaNivelAcesso();
            for (ConfigNivelAcesso config : categoria.getConfigNivelAcessoList()) {
                if (Boolean.TRUE.equals(config.getStatus())) {
                    authorities.add(new SimpleGrantedAuthority(normalizar(config.getDescricao())));
                }
            }
        }

        return User.withUsername(usuario.getEmail())
                .password(usuario.getSenha())
                .authorities(authorities)
                .build();
    }

    private String normalizar(String valor) {
        return valor.trim().toUpperCase().replace(" ", "_").replaceAll("[^A-Z0-9_]", "");
    }
}