package com.medscan.backend.DTO.Response;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoResponse(
        Long id,
        PacienteResponse paciente,
        AtendenteResponse atendente,
        TipoServicoResponse tipoServico,
        LocalDate diaCadastro,
        LocalTime horaCadastro,
        LocalDate diaAgendamento,
        LocalTime horaAgendamento,
        Integer escalaDor,
        String sintomas,
        String classificacaoRisco,
        BuscaAtivaResponse buscaAtiva,
        String status
) {}