package com.medscan.backend.DTO.Response;

import java.util.List;

public record UnidadeSaudeResponse(
        Long cnes,
        String nome,
        EnderecoResponse endereco
) {
}
