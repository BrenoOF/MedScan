package com.medscan.backend.Repository.JPA;

import com.medscan.backend.Model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // Adicione esta linha para o ArquivoService conseguir buscar o paciente logado
    Optional<Paciente> findByUsuarioId(Long usuarioId);
}