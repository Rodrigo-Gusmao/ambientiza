package com.ambientiza.api.repositories;

import com.ambientiza.api.models.ResgateRecompensas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResgateRecompensaRepository extends JpaRepository<ResgateRecompensas, Long> {

    List<ResgateRecompensas> findByUsuarioIdUsuarioOrderByDataResgateDesc(Long idUsuario);

    List<ResgateRecompensas> findByRecompensaIdRecompensaOrderByDataResgateDesc(Long idRecompensa);

    boolean existsByUsuarioIdUsuarioAndRecompensaIdRecompensa(Long idUsuario, Long idRecompensa);
}
