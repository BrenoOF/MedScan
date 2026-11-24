package com.medscan.backend.Repository.JPA;

import com.medscan.backend.Model.TipoNivelAcesso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TipoNivelAcessoRepository extends JpaRepository<TipoNivelAcesso, Long> {
    List<TipoNivelAcesso> findByNivelAcessoId(Long nivelId);
}
