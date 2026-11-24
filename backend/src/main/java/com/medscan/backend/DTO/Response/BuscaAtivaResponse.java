package com.medscan.backend.DTO.Response;

public record BuscaAtivaResponse(
    Long id,
    AgendamentoResponse agendamento,
    String descricao
){}
