package com.medscan.backend.DTO.Request;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoRequest(
    Long buscaAtivaId,
    Long pacienteId,
    Long atendenteId,
    Long tipoServicoId,
    LocalDate diaAgendamento,
    LocalTime horaAgendamento,
    Integer escalaDor,
    String sintomas,
    String classificacaoRisco,
    String status
){}
