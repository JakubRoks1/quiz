package com.jakubroks.quiz.service;

import com.jakubroks.quiz.dto.QuestionDTO;
import com.jakubroks.quiz.entity.Question;
import com.jakubroks.quiz.repository.QuestionRepository;
import com.jakubroks.quiz.repository.QuizRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    public QuestionService(QuestionRepository questionRepository, QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
    }

    public Question addQuestion(QuestionDTO dto) {
        Question question = new Question();
        question.setText(dto.text());
        question.setCorrectAnswer(dto.correctAnswer());
        return questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    public Question updateQuestion(Long id, QuestionDTO dto) {
        Question question = questionRepository.findById(id).orElseThrow();
        question.setText(dto.text());
        question.setCorrectAnswer(dto.correctAnswer());
        return questionRepository.save(question);
    }
}
