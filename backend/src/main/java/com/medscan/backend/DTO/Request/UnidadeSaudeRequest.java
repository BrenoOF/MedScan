package com.medscan.backend.DTO.Request;

public record UnidadeSaudeRequest(
        Long cnes,
        String nome,
        Long enderecoId
) {
}
