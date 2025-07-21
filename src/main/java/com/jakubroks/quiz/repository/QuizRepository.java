package com.jakubroks.quiz.repository;

import com.jakubroks.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByTitle(String title);
}
