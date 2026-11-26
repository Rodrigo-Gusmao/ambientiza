package com.ambientiza.api.repositories;

import com.ambientiza.api.models.QuizPerguntas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizPerguntasRepository extends JpaRepository<QuizPerguntas, Long> {
}
