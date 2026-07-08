package com.jakubroks.quiz.controller;

import com.jakubroks.quiz.entity.User;
import com.jakubroks.quiz.entry.GameEntry;
import com.jakubroks.quiz.input.AnswerInput;
import com.jakubroks.quiz.input.GameInput;
import com.jakubroks.quiz.repository.SavedGameEntryRepository;
import com.jakubroks.quiz.service.AuthService;
import com.jakubroks.quiz.service.GameService;
import com.jakubroks.quiz.service.ReportService;
import com.jakubroks.quiz.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @MockitoBean
    private GameService gameService;

    @MockitoBean
    private ReportService reportService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private SavedGameEntryRepository savedGameEntryRepository;

    @MockitoBean
    private AuthService authService;

    @Test
    void givenUserAndGameInput_whenStartingGame_thenReturnsGameEntry() {
        User user = new User();
        user.setId(1L);

        GameEntry entry = new GameEntry(
                UUID.randomUUID(),
                null,
                null,
                null
        );

        when(gameService.startGame(eq("1"), any(GameInput.class))).thenReturn(entry);

        assertThat(mockMvc.post().uri("/game/start")
                .requestAttr("user", user)
                .contentType("application/json")
                .content("""
                        {
                          "quizName": "Geography Quiz",
                          "size": 5,
                          "difficulty": "EASY"
                        }
                        """))
                .hasStatusOk();

        verify(gameService).startGame(eq("1"), any(GameInput.class));
    }

    @Test
    void givenValidGameInput_whenStartingGame_thenReturnsFirstQuestion() {
        User user = new User();
        user.setId(1L);

        UUID gameId = UUID.randomUUID();

        GameEntry entry = new GameEntry(
                gameId,
                "Question 1: What is the capital of Poland?"
        );

        when(gameService.startGame(eq("1"), any(GameInput.class))).thenReturn(entry);

        var result = mockMvc.post().uri("/game/start")
                .requestAttr("user", user)
                .contentType("application/json")
                .content("""
                        {
                          "quizName": "Geography Quiz",
                          "size": 5,
                          "difficulty": "EASY"
                        }
                        """);

        assertThat(result)
                .hasStatusOk();

        assertThat(result)
                .bodyJson()
                .extractingPath("$.id").isEqualTo(gameId.toString());

        assertThat(result)
                .bodyJson()
                .extractingPath("$.question").isEqualTo("Question 1: What is the capital of Poland?");
    }

    @Test
    void givenValidGameAnswer_whenSubmittingAnswer_thenReturnsNextQuestion() {
        User user = new User();
        user.setId(1L);

        UUID gameId = UUID.randomUUID();

        GameEntry nextEntry = new GameEntry(
                gameId,
                "Question 2: What is the capital of Germany?"
        );

        when(gameService.submitAnswer(eq("1"), any(AnswerInput.class))).thenReturn(nextEntry);

        var result = mockMvc.post().uri("/game/answer")
                .requestAttr("user", user)
                .contentType("application/json")
                .content("""
                        {
                          "id": "%s",
                          "answer": "Berlin"
                        }
                        """.formatted(gameId));

        assertThat(result)
                .hasStatusOk();

        assertThat(result)
                .bodyJson()
                .extractingPath("$.question")
                .isEqualTo("Question 2: What is the capital of Germany?");
    }

    @Test
    void givenInvalidJson_whenStartingGame_thenReturnsBadRequest() {
        User user = new User();
        user.setId(1L);

        var result = mockMvc.post().uri("/game/start")
                .requestAttr("user", user)
                .contentType("application/json")
                .content("""
                {
                  "quizName": "Geography Quiz",
                  "size": 5,
                  "difficulty": "EASY",
                }
                """);

        assertThat(result)
                .hasStatus(400);

    }

    @Test
    void givenInvalidJson_whenSubmittingAnswer_thenReturnsBadRequest() {
        User user = new User();
        user.setId(1L);

        var result = mockMvc.post().uri("/game/answer")
                .requestAttr("user", user)
                .contentType("application/json")
                .content("""
                {
                  "id": "1233231",
                  "answer": "Berlin",
                }
                """);

        assertThat(result)
                .hasStatus(400);

    }

}



