package com.jakubroks.quiz.service;

import com.jakubroks.quiz.PendingGame;
import com.jakubroks.quiz.dto.Person;
import com.jakubroks.quiz.entity.Question;
import com.jakubroks.quiz.entity.Quiz;
import com.jakubroks.quiz.entry.GameEntry;
import com.jakubroks.quiz.exception.GameAlreadyStartedException;
import com.jakubroks.quiz.exception.GameNotFoundException;
import com.jakubroks.quiz.exception.QuizNotFoundException;
import com.jakubroks.quiz.exception.TooManyQuestionsRequestedException;
import com.jakubroks.quiz.input.AnswerInput;
import com.jakubroks.quiz.input.GameInput;
import com.jakubroks.quiz.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    private final Map<String, PendingGame> userGames = new ConcurrentHashMap<>();
    private final QuizRepository quizRepository;

    public GameService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    private static final List<Person> AVAILABLE_PEOPLE = List.of(
            new Person(1L, "Jan"),
            new Person(2L, "Anna"),
            new Person(3L, "Piotr")
    );

    public GameEntry startGame(String userId, GameInput gameInput) {
        if (userGames.containsKey(userId)) {
            throw new GameAlreadyStartedException("Game already started for this user");
        }

        Quiz quiz = quizRepository.findByTitle(gameInput.quizName())
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found"));

        if (gameInput.size() > quiz.getQuestions().size()) {
            throw new TooManyQuestionsRequestedException("Requested more questions than available in quiz");
        }

        List<Question> questions = new ArrayList<>(quiz.getQuestions());
        Collections.shuffle(questions);
        List<Question> selectedQuestions = questions.stream().limit(gameInput.size()).toList();

        PendingGame pendingGame = new PendingGame(UUID.randomUUID(), quiz, selectedQuestions);
        userGames.put(userId, pendingGame);

        String msg = "Pytanie 1: " + selectedQuestions.get(0).getText();
        return new GameEntry(pendingGame.getId(), msg);
    }

    public GameEntry submitAnswer(String userId, AnswerInput answerInput) {
        PendingGame pendingGame = userGames.get(userId);

        if (pendingGame == null || !pendingGame.getId().equals(answerInput.id())) {
            throw new GameNotFoundException("Game not started or invalid id for this user");
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
            userGames.remove(userId);
            return result;
        }
    }
}
