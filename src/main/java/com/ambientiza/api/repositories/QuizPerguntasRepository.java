package com.ambientiza.api.repositories;

import com.ambientiza.api.models.Quiz;
import com.ambientiza.api.models.QuizPerguntas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizPerguntasRepository extends JpaRepository<QuizPerguntas, Long> {
    List<QuizPerguntas> findByQuiz(Quiz quiz);
    List<QuizPerguntas> findByQuizIdQuiz(Long idQuiz);
}
