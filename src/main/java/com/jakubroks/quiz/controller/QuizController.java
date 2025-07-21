package com.jakubroks.quiz.controller;

import com.jakubroks.quiz.dto.QuizDTO;
import com.jakubroks.quiz.entity.Quiz;
import com.jakubroks.quiz.service.QuizService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/{id}")
    public Quiz getQuiz(@PathVariable Long id) {
        return quizService.getQuiz(id);
    }

    @PostMapping
    public Quiz addQuiz(@RequestBody QuizDto dto) {
        return quizService.addQuiz(new QuizDTO(dto.quizId(), dto.title()));
    }

    @GetMapping
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @DeleteMapping("/{id}")
    public void deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
    }

    @PutMapping("/{id}")
    public Quiz updateQuiz(@PathVariable Long id, @RequestBody QuizDto dto) {
        return quizService.updateQuiz(id, dto);
    }

    public record QuizDto(Long quizId, String title) {
    }
}
