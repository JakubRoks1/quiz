package com.jakubroks.quiz.config;

import com.jakubroks.quiz.entity.Quiz;
import com.jakubroks.quiz.entity.Question;
import com.jakubroks.quiz.repository.QuizRepository;
import com.jakubroks.quiz.repository.QuestionRepository;
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

        var scienceQuiz = new Quiz();
        scienceQuiz.setTitle("Science Quiz");

        Set<Question> scienceQuestions = new HashSet<>();
        scienceQuestions.add(saveQuestion("What planet is known as the Red Planet?", "Mars"));
        scienceQuestions.add(saveQuestion("What is the chemical symbol for water?", "H2O"));
        scienceQuestions.add(saveQuestion("What gas do plants absorb from the atmosphere?", "Carbon dioxide"));
        scienceQuestions.add(saveQuestion("How many bones are in the adult human body?", "206"));
        scienceQuestions.add(saveQuestion("What force keeps us on the ground?", "Gravity"));
        scienceQuestions.add(saveQuestion("What is the largest organ in the human body?", "Skin"));
        scienceQuestions.add(saveQuestion("What particle carries a negative charge?", "Electron"));
        scienceQuestions.add(saveQuestion("Who developed the theory of relativity?", "Albert Einstein"));
        scienceQuestions.add(saveQuestion("What is the boiling point of water at sea level in Celsius?", "100"));
        scienceQuestions.add(saveQuestion("What is the process by which plants make their food?", "Photosynthesis"));

        scienceQuiz.setQuestions(scienceQuestions);
        quizRepository.save(scienceQuiz);
    }

    private Question saveQuestion(String text, String correctAnswer) {
        var question = new Question();
        question.setText(text);
        question.setCorrectAnswer(correctAnswer);
        return questionRepository.save(question);
    }
}