package com.medscan.backend.DTO.Request;

public record UsuarioRequest(
        Long id,
        String nome,
        String email,
        String senha,
        String telefone,
        Long nivelAcessoId

) {

}

