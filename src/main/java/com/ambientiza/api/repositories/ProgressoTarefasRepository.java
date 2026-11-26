package com.ambientiza.api.repositories;

import com.ambientiza.api.models.ProgressoTarefas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProgressoTarefasRepository extends JpaRepository<ProgressoTarefas, Long> {
    List<ProgressoTarefas> findByUsuarioIdUsuarioAndDataConclusaoGreaterThanEqual(Long idUsuario, LocalDate desde);

    boolean existsByUsuarioIdUsuarioAndTarefaIdTarefaAndDataConclusaoGreaterThanEqual(Long idUsuario, Long idTarefa, LocalDate desde);

    long countByUsuarioIdUsuario(Long idUsuario);

    boolean existsByTarefaIdTarefa(Long idTarefa);
}
