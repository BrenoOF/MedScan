package com.medscan.backend.Repository.JPA;


import com.medscan.backend.Model.Funcao;
import com.medscan.backend.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncaoRepository extends JpaRepository<Funcao, Long> {

    Funcao findFuncaoById(Long id);
}
