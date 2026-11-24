package com.medscan.backend.DTO.Response;

import java.util.List;

public record AtendenteResponse(
        Long id,
        Long usuarioId,
        Long unidadeSaudeId,
        List<AgendamentoResponse> agendamentoList
) {}
