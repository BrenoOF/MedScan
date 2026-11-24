package com.medscan.backend.Repository.JPA;

import com.medscan.backend.Model.UnidadeSaude;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UnidadeSaudeRepository extends JpaRepository<UnidadeSaude, Long> {
    UnidadeSaude findByCnes(Long cnes);

    List<UnidadeSaude> findAllByNomeContainingIgnoreCase(String nome);
}
