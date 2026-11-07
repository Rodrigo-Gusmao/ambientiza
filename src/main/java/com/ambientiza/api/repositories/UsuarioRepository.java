package com.ambientiza.api.repositories;

import com.ambientiza.api.models.UsuariosModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuariosModel, Long> {
}
