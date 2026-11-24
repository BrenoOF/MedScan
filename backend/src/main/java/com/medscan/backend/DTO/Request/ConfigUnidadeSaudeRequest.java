package com.medscan.backend.DTO.Request;

public record ConfigUnidadeSaudeRequest(
        String descricao,
        Long categoriaUnidadeSaudeId,
        Boolean status
) {}
