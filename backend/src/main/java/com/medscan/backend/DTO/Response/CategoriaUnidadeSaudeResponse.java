package com.medscan.backend.DTO.Response;

import java.util.List;

public record CategoriaUnidadeSaudeResponse(
        Long id,
        String descricao,
        List<Long> tipoUnidadeSaudeIds,
        List<Long> configUnidadeSaudeIds
) {}
