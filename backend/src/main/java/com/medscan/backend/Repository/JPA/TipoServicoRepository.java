package com.medscan.backend.Repository.JPA;

import com.medscan.backend.DTO.Response.TipoAgendamentoResponse;
import com.medscan.backend.Model.TipoAgendamento;
import com.medscan.backend.Model.TipoServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TipoServicoRepository extends JpaRepository<TipoServico, Long> {
    List<TipoServico> findByUnidadeSaudeCnesEquals(Long unidadeSaudeCnes);

    @Query("""
          SELECT ta.id, ta.descricao
          FROM TipoServico ts
          RIGHT JOIN TipoAgendamento ta ON ts.tipoAgendamento.id = ta.id
          WHERE ts.unidadeSaude.cnes = :cnes
    """)
    List<TipoAgendamentoResponse> buscarPorCnes(@Param("cnes") Long cnes);


}
