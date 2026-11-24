package com.medscan.backend.Repository.JPA;

import com.medscan.backend.Model.NivelAcesso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface NivelAcessoRepository extends JpaRepository<NivelAcesso, Long> {
    List<NivelAcesso> findByDescricaoContainingIgnoreCase(String nome);
}
