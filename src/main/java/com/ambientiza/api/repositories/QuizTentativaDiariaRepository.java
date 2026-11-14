package com.ambientiza.api.repositories;

import com.ambientiza.api.models.QuizTentativaDiaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface QuizTentativaDiariaRepository extends JpaRepository<QuizTentativaDiaria, Long> {
    boolean existsByUsuarioIdUsuarioAndQuizIdQuizAndDataJogada(Long idUsuario, Long idQuiz, LocalDate dataJogada);
    Optional<QuizTentativaDiaria> findTopByUsuarioIdUsuarioOrderByDataJogadaDesc(Long idUsuario);
}
