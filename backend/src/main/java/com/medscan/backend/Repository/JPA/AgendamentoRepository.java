package com.medscan.backend.Repository.JPA;

import com.medscan.backend.Model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findAllByAtendenteId(Long atendenteId);
}