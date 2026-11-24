package com.medscan.backend.DTO.Request;

public record TipoUnidadeSaudeRequest(
        Long unidadeSaudeId,
        Long categoriaUnidadeSaudeId
) {}
