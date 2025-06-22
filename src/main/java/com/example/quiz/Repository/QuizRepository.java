package com.example.quiz.Repository;

import com.example.quiz.Entity.Question;
import com.example.quiz.Entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
