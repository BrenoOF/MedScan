package com.medscan.backend.DTO.Response;

import java.time.LocalDate;
import java.util.List;

public record PacienteResponse(
        Long id,
        UsuarioResponse usuario,
        String cpf,
        LocalDate dataNascimento,
        String genero,
        EnderecoResponse endereco,
        List<AgendamentoResponse> agendamentoList
) {
}
