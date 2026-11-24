package com.medscan.backend.DTO.Response;

public record ConfigNivelAcessoResponse(
        Long id,
        String descricao,
        Long categoriaNivelAcesso,
        Boolean status
) {
}
