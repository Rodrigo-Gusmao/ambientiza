package com.ambientiza.api.repositories;

import com.ambientiza.api.models.QuizAlternativas;
import com.ambientiza.api.models.QuizPerguntas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAlternativasRepository extends JpaRepository<QuizAlternativas, Long> {
    List<QuizAlternativas> findByQuizPerguntas(QuizPerguntas quizPerguntas);
    List<QuizAlternativas> findByQuizPerguntasIdPergunta(Long idPergunta);
}
