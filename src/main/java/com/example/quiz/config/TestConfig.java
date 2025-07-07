package com.example.quiz.config;

import com.example.quiz.entity.Quiz;
import com.example.quiz.entity.Question;
import com.example.quiz.repository.QuizRepository;
import com.example.quiz.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class TestConfig {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public TestConfig(QuizRepository quizRepository, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    @PostConstruct
    public void a() {
        var geographyQuiz = new Quiz();
        geographyQuiz.setTitle("Geography Quiz");

        Set<Question> geographyQuestions = new HashSet<>();

        geographyQuestions.add(saveQuestion("What is the capital of France?", "Paris"));
        geographyQuestions.add(saveQuestion("Which continent is the Sahara Desert located on?", "Africa"));
        geographyQuestions.add(saveQuestion("Which is the largest country in the world by area?", "Russia"));
        geographyQuestions.add(saveQuestion("What is the longest river in the world?", "Nile"));
        geographyQuestions.add(saveQuestion("Which ocean lies on the east coast of the United States?", "Atlantic Ocean"));
        geographyQuestions.add(saveQuestion("Mount Everest is located in which mountain range?", "Himalayas"));
        geographyQuestions.add(saveQuestion("What is the capital city of Australia?", "Canberra"));
        geographyQuestions.add(saveQuestion("Which country has the most islands in the world?", "Sweden"));
        geographyQuestions.add(saveQuestion("Which U.S. state is known as the 'Sunshine State'?", "Florida"));
        geographyQuestions.add(saveQuestion("What is the smallest country in the world?", "Vatican City"));

        geographyQuiz.setQuestions(geographyQuestions);
        quizRepository.save(geographyQuiz);
    }

    private Question saveQuestion(String text, String correctAnswer) {
        var question = new Question();
        question.setText(text);
        question.setCorrectAnswer(correctAnswer);
        return questionRepository.save(question);
    }
}