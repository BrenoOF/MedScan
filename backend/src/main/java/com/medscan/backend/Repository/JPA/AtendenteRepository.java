package com.medscan.backend.Repository.JPA;

import com.medscan.backend.Model.Atendente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtendenteRepository extends JpaRepository<Atendente, Long> {
}
