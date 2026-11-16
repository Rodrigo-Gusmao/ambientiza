package com.ambientiza.api.repositories;

import com.ambientiza.api.models.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuarios, Long> {

    Optional<Usuarios> findByEmail(String email);

    Optional<Usuarios> findByNome(String nome);

    boolean existsByEmail(String email);

    boolean existsByNome(String nome);
}
