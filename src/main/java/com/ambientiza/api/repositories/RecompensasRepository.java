package com.ambientiza.api.repositories;

import com.ambientiza.api.models.Recompensas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecompensasRepository extends JpaRepository<Recompensas, Long> {

    Optional<Recompensas> findByNome(String nome);

    boolean existsByNome(String nome);

    List<Recompensas> findByEstoqueGreaterThan(Integer estoqueMinimo);
}
