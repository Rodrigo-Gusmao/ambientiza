package com.ambientiza.api.repositories;

import com.ambientiza.api.models.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuarios, Long> {
}
