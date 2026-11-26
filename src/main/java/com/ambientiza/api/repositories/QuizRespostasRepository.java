package com.ambientiza.api.repositories;

import com.ambientiza.api.models.QuizRespostas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRespostasRepository extends JpaRepository<QuizRespostas, Long> {
    List<QuizRespostas> findByUsuarioIdUsuarioAndPerguntaQuizIdQuiz(Long idUsuario, Long idQuiz);

    boolean existsByUsuarioIdUsuarioAndPerguntaIdPergunta(Long idUsuario, Long idPergunta);

    boolean existsByPerguntaQuizIdQuiz(Long idQuiz);
}
