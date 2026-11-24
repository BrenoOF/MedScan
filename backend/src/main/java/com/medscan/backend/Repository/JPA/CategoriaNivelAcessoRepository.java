package com.medscan.backend.Repository.JPA;

import com.medscan.backend.Model.CategoriaNivelAcesso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CategoriaNivelAcessoRepository extends JpaRepository<CategoriaNivelAcesso, Long> {
    List<CategoriaNivelAcesso> findAll();
}
