package com.medscan.backend.Repository.JPA;

import com.medscan.backend.Model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
}
