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

    // Busca quizzes de um tema específico (ex: RECICLAGEM, ENERGIA, AGUA)
    List<Quiz> findByTema(Temas tema);

    // Busca o quiz programado para uma data específica (ex: Desafio Diário)
    Optional<Quiz> findByDataDisponivel(LocalDate dataDisponivel);

    // Lista apenas quizzes que já estão liberados para os jogadores até a data de hoje
    List<Quiz> findByDataDisponivelLessThanEqual(LocalDate dataAtual);

    // Verifica se já existe um quiz agendado para determinada data com o mesmo título
    boolean existsByTituloIgnoreCaseAndDataDisponivel(String titulo, LocalDate dataDisponivel);
}