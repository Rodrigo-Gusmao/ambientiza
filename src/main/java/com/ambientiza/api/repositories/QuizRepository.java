package com.ambientiza.api.repositories;

import com.ambientiza.api.models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByDataDisponivelBetweenOrderByDataDisponivelDescIdQuizDesc(LocalDate inicio, LocalDate fim);

    List<Quiz> findAllByOrderByDataDisponivelDesc();
}
