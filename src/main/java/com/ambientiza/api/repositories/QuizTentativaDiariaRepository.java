package com.ambientiza.api.repositories;

import com.ambientiza.api.models.QuizTentativaDiaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuizTentativaDiariaRepository extends JpaRepository<QuizTentativaDiaria, Long> {

    boolean existsByUsuarioIdUsuarioAndQuizIdQuizAndDataJogada(Long idUsuario, Long idQuiz, LocalDate dataJogada);

    boolean existsByUsuarioIdUsuarioAndDataJogada(Long idUsuario, LocalDate dataJogada);

    Optional<QuizTentativaDiaria> findByUsuarioIdUsuarioAndQuizIdQuizAndDataJogada(Long idUsuario, Long idQuiz, LocalDate dataJogada);

    List<QuizTentativaDiaria> findByUsuarioIdUsuarioOrderByDataJogadaDesc(Long idUsuario);
}
