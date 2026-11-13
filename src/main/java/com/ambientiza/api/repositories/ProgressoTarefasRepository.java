package com.ambientiza.api.repositories;

import com.ambientiza.api.models.ProgressoTarefas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressoTarefasRepository extends JpaRepository<ProgressoTarefas, Long> {
}
