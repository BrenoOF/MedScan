package com.medscan.backend.DTO.Request;

public record TipoServicoRequest(
        Long unidadeSaudeId,
        Long tipoAgendamentoId
) {
}
