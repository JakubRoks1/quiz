package com.example.quiz.service;

import com.example.quiz.PendingGame;
import com.example.quiz.dto.Person;
import com.example.quiz.entity.Question;
import com.example.quiz.entity.Quiz;
import com.example.quiz.entry.GameEntry;
import com.example.quiz.input.AnswerInput;
import com.example.quiz.input.GameInput;
import com.example.quiz.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class GameService {


    private PendingGame pendingGame;


    private final QuizRepository quizRepository;

    public GameService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    private static final List<Person> AVAILABLE_PEOPLE = List.of(
            new Person(1L, "Jan"),
            new Person(2L, "Anna"),
            new Person(3L, "Piotr")
    );

    public GameEntry startGame(GameInput gameInput) {
        if (pendingGame != null) {
            throw new IllegalStateException("Game already started");
        }

        Quiz quiz = quizRepository.findByTitle(gameInput.quizName())
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        List<Question> questions = new ArrayList<>(quiz.getQuestions());
        Collections.shuffle(questions);
        List<Question> selectedQuestions = questions.stream().limit(gameInput.size()).toList();

        pendingGame = new PendingGame(UUID.randomUUID(), quiz, selectedQuestions);

        String msg = "Pytanie 1: " + selectedQuestions.get(0).getText();
        return new GameEntry(pendingGame.getId(), msg);
    }

    public GameEntry submitAnswer(AnswerInput answerInput) {
        if (pendingGame == null || !pendingGame.getId().equals(answerInput.id())) {
            throw new IllegalStateException("Game not started or invalid id");
        }

        boolean isOngoing = pendingGame.submitAnswer(answerInput.answer());
        if (isOngoing) {
            int nextQuestionIndex = pendingGame.getAnswers().size();
            Question nextQuestion = pendingGame.getQuestions().get(nextQuestionIndex);

            return new GameEntry(pendingGame.getId(), "Pytanie " + (nextQuestionIndex + 1) + ": " + nextQuestion.getText());
        } else {
            GameEntry result = new GameEntry(
                    pendingGame.getId(),
                    pendingGame.getQuestions(),
                    pendingGame.getAnswers(),
                    pendingGame.getScore()
            );
            pendingGame = null;
            return result;
        }
    }


}
