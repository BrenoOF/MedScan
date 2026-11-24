package com.medscan.backend.Repository.JPA;

import com.medscan.backend.Model.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {
    List<Arquivo> findByTipoEntidadeAndIdEntidade(Arquivo.TipoEntidade tipoEntidade, Long idEntidade);
    List<Arquivo> findByTipoArquivoAndTipoEntidadeAndIdEntidade(Arquivo.TipoArquivo tipoArquivo, Arquivo.TipoEntidade tipoEntidade, Long idEntidade);
    Optional<Arquivo> findByNomeArquivo(String nomeArquivo);
}
