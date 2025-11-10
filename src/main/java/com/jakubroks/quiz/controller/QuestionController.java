package com.jakubroks.quiz.controller;

import com.jakubroks.quiz.dto.QuestionDTO;
import com.jakubroks.quiz.entity.Question;
import com.jakubroks.quiz.service.AuthService;
import com.jakubroks.quiz.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final AuthService authService;

    public QuestionController(QuestionService questionService, AuthService authService) {
        this.questionService = questionService;
        this.authService = authService;
    }

    @PostMapping
    public Question addQuestion(@RequestBody QuestionDTO dto) {
        return questionService.addQuestion(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
    }

    @PutMapping("/{id}")
    public Question updateQuestion(@PathVariable Long id, @RequestBody QuestionDTO dto) {
        return questionService.updateQuestion(id, dto);
    }
}
