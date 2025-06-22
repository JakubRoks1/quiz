package com.example.quiz.Controller;

import com.example.quiz.Entity.Question;
import com.example.quiz.Entity.Quiz;
import com.example.quiz.Repository.QuestionRepository;
import com.example.quiz.Repository.QuizRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/quizzes")
public class QuizController {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/{id}")
    public Quiz getQuiz(@PathVariable Long id) {
        return quizRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public Quiz addQuiz(@RequestBody QuizDto dto) {
        Quiz quiz = new Quiz();
        quiz.setTitle(dto.title());
        return quizRepository.save(quiz);
    }

    public record QuizDto(Long quizId, String title) {
    }
}
