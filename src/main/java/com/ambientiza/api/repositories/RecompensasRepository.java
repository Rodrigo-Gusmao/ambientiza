package com.ambientiza.api.repositories;

import com.ambientiza.api.models.Recompensas;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RecompensasRepository extends JpaRepository<Recompensas, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Recompensas r where r.idRecompensa = :id")
    Optional<Recompensas> findByIdParaAtualizar(Long id);
}
