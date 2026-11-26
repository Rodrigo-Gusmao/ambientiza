package com.ambientiza.api.repositories;

import com.ambientiza.api.models.Tarefas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefasRepository extends JpaRepository<Tarefas, Long> {
}
