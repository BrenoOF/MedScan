package com.medscan.backend.DTO.Response;

public record ConfigUnidadeSaudeResponse(
        Long id,
        String descricao,
        Long categoriaUnidadeSaudeId,
        String categoriaUnidadeSaudeDescricao,
        Boolean status
) {}
