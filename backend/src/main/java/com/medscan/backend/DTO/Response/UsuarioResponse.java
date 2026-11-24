package com.medscan.backend.DTO.Response;

import java.util.List;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        NivelAcessoResponse nivelAcesso
) {
}
