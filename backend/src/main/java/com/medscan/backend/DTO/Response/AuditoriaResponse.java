package com.medscan.backend.DTO.Response;

import java.time.LocalDate;
import java.time.LocalTime;

public record AuditoriaResponse(
        Long id,
        String acao,
        LocalDate diaCadastro,
        LocalTime horaCadastro,
        String entidade,
        Long idEntidade,
        UsuarioResponse usuario
) {}
