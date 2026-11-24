package com.medscan.backend.Repository.JPA;

import com.medscan.backend.Model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
