package com.jakubroks.quiz.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GameServiceTest {

    private QuizRepository quizRepository;
    private GameService gameService;

    @BeforeEach
    void setUp() {
        quizRepository = mock(QuizRepository.class);
        gameService = new GameService(quizRepository);
    }

    @Test
    void givenNonExistingQuiz_whenStartGame_thenShouldThrowQuizNotFoundException() {
        String userId = "user3";
        when(quizRepository.findByTitle("Geography")).thenReturn(Optional.empty());
        GameInput input = new GameInput("Geography", 1);

        assertThatThrownBy(() -> gameService.startGame(userId, input))
                .isInstanceOf(QuizNotFoundException.class)
                .hasMessageContaining("Quiz not found");
    }

    @Test
    void givenNoGameForUser_whenSubmitAnswer_thenShouldThrowGameNotFoundException() {
        String userId = "user6";
        AnswerInput answerInput = new AnswerInput(UUID.randomUUID(), "A");
        assertThatThrownBy(() -> gameService.submitAnswer(userId, answerInput))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessageContaining("Game not started");
    }

    @Test
    void givenTooManyQuestionsRequested_whenStartGame_thenShouldThrowTooManyQuestionsRequestedException() {
        String userId = "user3";
        GameInput input = new GameInput("quiz", 5);

        Quiz quiz = new Quiz();
        quiz.setQuestions(Set.of(new Question(), new Question())); // only 2 questions

        when(quizRepository.findByTitle("quiz")).thenReturn(Optional.of(quiz));

        assertThatThrownBy(() -> gameService.startGame(userId, input))
                .isInstanceOf(TooManyQuestionsRequestedException.class);
    }

    @Test
    void givenValidQuizAndUser_whenStartGame_thenShouldStartGameSuccessfully() {
        String userId = "user4";
        GameInput input = new GameInput("quiz", 2);

        Question q1 = new Question();
        q1.setText("First?");
        Question q2 = new Question();
        q2.setText("Second?");
        Quiz quiz = new Quiz();
        quiz.setQuestions(Set.of(q1, q2));

        when(quizRepository.findByTitle("quiz")).thenReturn(Optional.of(quiz));

        GameEntry entry = gameService.startGame(userId, input);

        assertThat(entry).isNotNull();
        assertThat(entry.question()).contains("Pytanie 1");
    }

    @Test
    void givenGameAlreadyStartedForUser_whenStartGame_thenShouldThrowGameAlreadyStartedException() {
        String userId = "user1";
        GameInput input = new GameInput("quiz1", 1);

        Quiz quiz = new Quiz();
        quiz.setQuestions(Set.of(new Question()));
        when(quizRepository.findByTitle("quiz1")).thenReturn(Optional.of(quiz));
        gameService.startGame(userId, input);

        assertThatThrownBy(() -> gameService.startGame(userId, input))
                .isInstanceOf(GameAlreadyStartedException.class)
                .hasMessageContaining("Game already started for this user");
    }
}
