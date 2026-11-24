package com.medscan.backend.Repository.JPA;

import com.medscan.backend.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findAllByNomeContainingIgnoreCase(String nome);

    List<Usuario> findAllByEmailContainingIgnoreCase(String email);

    List<Usuario> findAllByNivelAcessoId(Long idNivel);

    Optional<Usuario> findUsuarioByEmailEqualsIgnoreCase(String email);

}
