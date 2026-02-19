package com.jakubroks.quiz.controller;

import com.jakubroks.quiz.dto.QuizDTO;
import com.jakubroks.quiz.entity.Quiz;
import com.jakubroks.quiz.picker.QuestionPicker;
import com.jakubroks.quiz.service.AuthService;
import com.jakubroks.quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizService quizService;
    private final AuthService authService;
    private final QuestionPicker questionPicker;

    @Autowired
    public QuizController(QuizService quizService, AuthService authService, QuestionPicker questionPicker) {
        this.quizService = quizService;
        this.authService = authService;
        this.questionPicker = questionPicker;
    }

    @GetMapping("/test/{id}")
    public void test(@PathVariable Long id, @RequestParam int size, @RequestParam String name) {
        questionPicker.pick(quizService.getQuiz(id), size, name);
    }

    @GetMapping("/{id}")
    public Quiz getQuiz(@PathVariable Long id) {
        return quizService.getQuiz(id);
    }

    @PostMapping
    public Quiz addQuiz(@RequestBody QuizDto dto) {
        return quizService.addQuiz(new QuizDTO(dto.title()));
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
