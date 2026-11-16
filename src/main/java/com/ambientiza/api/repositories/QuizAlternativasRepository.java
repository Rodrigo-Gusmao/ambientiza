package com.ambientiza.api.repositories;

import com.ambientiza.api.models.QuizAlternativas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAlternativasRepository extends JpaRepository<QuizAlternativas, Long> {

    List<QuizAlternativas> findByQuizPerguntasIdPergunta(Long idPergunta);

    Optional<QuizAlternativas> findByQuizPerguntasIdPerguntaAndCorretaTrue(Long idPergunta);
}
