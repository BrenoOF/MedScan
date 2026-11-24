package com.medscan.backend.DTO.Response;

public record TipoUnidadeSaudeResponse(
        Long id,
        Long unidadeSaudeId,
        String unidadeSaudeNome,
        Long categoriaUnidadeSaudeId,
        String categoriaUnidadeSaudeDescricao
) {}
