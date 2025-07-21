package com.jakubroks.quiz.service;

import com.jakubroks.quiz.controller.QuizController;
import com.jakubroks.quiz.dto.QuizDTO;
import com.jakubroks.quiz.entity.Quiz;
import com.jakubroks.quiz.repository.QuestionRepository;
import com.jakubroks.quiz.repository.QuizRepository;
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
