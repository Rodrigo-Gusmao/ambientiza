package com.ambientiza.api.repositories;

import com.ambientiza.api.models.QuizTentativaDiaria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizTentativaDiariaRepository extends JpaRepository<QuizTentativaDiaria, Long> {
    Optional<QuizTentativaDiaria> findByUsuarioIdUsuarioAndQuizIdQuiz(Long idUsuario, Long idQuiz);

    long countByUsuarioIdUsuario(Long idUsuario);

    boolean existsByQuizIdQuiz(Long idQuiz);
}
