package com.medscan.backend.DTO.Request;

public record ConfigNivelAcessoRequest(
        String descricao,
        Long categoriaId,
        Boolean status
) {}