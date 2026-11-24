package com.medscan.backend.DTO.Request;

import java.util.List;

public record AtendenteRequest (
    Long usuarioId,
    Long unidadeSaudeId
) {}