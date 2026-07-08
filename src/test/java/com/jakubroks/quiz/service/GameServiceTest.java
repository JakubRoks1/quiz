package com.jakubroks.quiz.service;

import com.jakubroks.quiz.entity.Difficulty;
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
import com.jakubroks.quiz.repository.SavedGameEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GameServiceTest {

    private QuizRepository quizRepository;
    private GameService gameService;

    private SavedGameEntryRepository savedGameEntryRepository;

    private QuizCacheService quizCacheService;

    @BeforeEach
    void setUp() {
        quizRepository = mock(QuizRepository.class);
        savedGameEntryRepository = mock(SavedGameEntryRepository.class);
        quizCacheService = mock(QuizCacheService.class);
        gameService = new GameService(quizRepository, savedGameEntryRepository,quizCacheService);
    }

    @Test
    void givenNonExistingQuiz_whenStartGame_thenShouldThrowQuizNotFoundException() {
        String userId = "user3";
        GameInput input = new GameInput("Geography", 1, Difficulty.EASY);

        when(quizCacheService.getQuizForGame("Geography"))
                .thenThrow(new QuizNotFoundException("Quiz not found"));

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
        GameInput input = new GameInput("quiz", 5, Difficulty.EASY);

        Question q1 = new Question();
        q1.setText("First?");
        q1.setDifficulty(Difficulty.EASY);

        Question q2 = new Question();
        q2.setText("Second?");
        q2.setDifficulty(Difficulty.EASY);

        Quiz quiz = new Quiz();
        quiz.setTitle("quiz");
        quiz.setQuestions(Set.of(q1, q2));

        when(quizCacheService.getQuizForGame("quiz")).thenReturn(quiz);

        assertThatThrownBy(() -> gameService.startGame(userId, input))
                .isInstanceOf(TooManyQuestionsRequestedException.class);
    }

    @Test
    void givenValidQuizAndUser_whenStartGame_thenShouldStartGameSuccessfully() {
        String userId = "user4";
        GameInput input = new GameInput("Quiz", 2, Difficulty.EASY);

        Question q1 = new Question();
        q1.setText("First?");
        q1.setDifficulty(Difficulty.EASY);

        Question q2 = new Question();
        q2.setText("Second?");
        Quiz quiz = new Quiz();
        q2.setDifficulty(Difficulty.EASY);

        quiz.setQuestions(Set.of(q1, q2));

        when(quizCacheService.getQuizForGame("Quiz")).thenReturn(quiz);

        GameEntry entry = gameService.startGame(userId, input);

        assertThat(entry).isNotNull();
        assertThat(entry.id()).isNotNull();
        assertThat(entry.question()).startsWith("Question 1:");
        assertThat(entry.question()).containsAnyOf("First?", "Second?");
    }

    @Test
    void givenGameAlreadyStartedForUser_whenStartGame_thenShouldThrowGameAlreadyStartedException() {
        String userId = "user1";
        GameInput input = new GameInput("quiz1", 1, Difficulty.EASY);

        Question question = new Question();
        question.setText("Question?");
        question.setDifficulty(Difficulty.EASY);

        Quiz quiz = new Quiz();
        quiz.setTitle("quiz1");
        quiz.setQuestions(Set.of(question));

        when(quizCacheService.getQuizForGame("quiz1")).thenReturn(quiz);

        gameService.startGame(userId, input);

        assertThatThrownBy(() -> gameService.startGame(userId, input))
                .isInstanceOf(GameAlreadyStartedException.class)
                .hasMessageContaining("Game already started for this user");
    }
}
