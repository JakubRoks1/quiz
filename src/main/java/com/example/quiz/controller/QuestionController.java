package com.example.quiz.controller;

import com.example.quiz.entity.Question;
import com.example.quiz.repository.QuestionRepository;
import com.example.quiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuizRepository quizRepository;

    @PostMapping
    public Question addQuestion(@RequestBody QuestionDto dto) {
        Question question = new Question();
        question.setText(dto.text());
        question.setCorrectAnswer(dto.correctAnswer());
        return questionRepository.save(question);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        questionRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Question updateQuestion(@PathVariable Long id, @RequestBody QuestionDto dto) {
        Question question = questionRepository.findById(id).orElseThrow();
        question.setText(dto.text());
        question.setCorrectAnswer(dto.correctAnswer());
        return questionRepository.save(question);
    }


    record QuestionDto(Long quizId, String text, String correctAnswer) {}
}
