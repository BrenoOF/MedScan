package com.medscan.backend.DTO.Request;

public record AuditoriaRequest(
        String acao,
        String entidade,
        Long idEntidade,
        Long usuarioId
) {}
