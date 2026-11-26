package com.ambientiza.api.repositories;

import com.ambientiza.api.models.ResgateRecompensas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResgateRecompensaRepository extends JpaRepository<ResgateRecompensas, Long> {
    List<ResgateRecompensas> findByUsuarioIdUsuarioOrderByDataResgateDescIdResgateDesc(Long idUsuario);

    boolean existsByRecompensaIdRecompensa(Long idRecompensa);
}
