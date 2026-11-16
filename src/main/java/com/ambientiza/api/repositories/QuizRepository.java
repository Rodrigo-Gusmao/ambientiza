package com.ambientiza.api.repositories;

import com.ambientiza.api.enums.Temas;
import com.ambientiza.api.models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    List<Quiz> findByDataDisponivel(LocalDate dataDisponivel);

    Optional<Quiz> findFirstByDataDisponivelOrderByDataDisponivelDesc(LocalDate dataDisponivel);

    List<Quiz> findByTema(Temas tema);
}
