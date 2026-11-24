package com.medscan.backend.Repository.JPA;

import com.medscan.backend.Model.TipoAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoAgendamentoRepository extends JpaRepository<TipoAgendamento, Long> {
    Optional<TipoAgendamento> findById(Long id);
}
