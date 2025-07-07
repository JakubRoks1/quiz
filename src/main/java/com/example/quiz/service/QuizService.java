package com.example.quiz.service;

import com.example.quiz.controller.QuizController;
import com.example.quiz.dto.QuizDTO;
import com.example.quiz.entity.Quiz;
import com.example.quiz.repository.QuestionRepository;
import com.example.quiz.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {
    private final QuizRepository quizRepository;

    private final QuestionRepository questionRepository;

    public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    public Quiz addQuiz(QuizDTO dto) {
        Quiz quiz = new Quiz();
        quiz.setTitle(dto.title());
        return quizRepository.save(quiz);
    }

    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    public List<Quiz> getAllQuizzes(){
        return quizRepository.findAll();
    }

    public Quiz getQuiz(Long id){
        return quizRepository.findById(id).orElseThrow();
    }

    public Quiz updateQuiz(Long id, QuizController.QuizDto dto) {
        Quiz quiz = quizRepository.findById(id).orElseThrow();
        quiz.setTitle(dto.title());
        return quizRepository.save(quiz);
    }
}
