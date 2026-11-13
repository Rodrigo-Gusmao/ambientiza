package com.ambientiza.api.repositories;

import com.ambientiza.api.models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
