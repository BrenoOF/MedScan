package com.medscan.backend.DTO.Request;

import java.time.LocalDate;

public record PacienteRequest(
        Long usuarioId,
        String cpf,
        LocalDate dataNascimento,
        String genero,
        Long enderecoId
) {
}
