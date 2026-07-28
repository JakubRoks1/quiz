package com.jakubroks.quiz.integration;

import com.jakubroks.quiz.dto.QuizResultDTO;
import com.jakubroks.quiz.entity.Difficulty;
import com.jakubroks.quiz.entity.Question;
import com.jakubroks.quiz.entity.Quiz;
import com.jakubroks.quiz.entity.User;
import com.jakubroks.quiz.entry.GameEntry;
import com.jakubroks.quiz.input.AnswerInput;
import com.jakubroks.quiz.service.QuizCacheService;
import com.jakubroks.quiz.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class GameFlowIntegrationTest {

    private static final String TEST_KEY = "test-key";
    private static final String ZERO_SCORE_KEY = "zero-score-key";
    private static final String QUIZ_NAME = "Difficulty";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private QuizCacheService quizCacheService;

    @BeforeEach
    void setUp() {
        User testUser = createTestUser();
        User zeroScoreUser = createZeroScoreUser();

        given(userService.getByKey(TEST_KEY))
                .willReturn(Optional.of(testUser));

        given(userService.getByKey(ZERO_SCORE_KEY))
                .willReturn(Optional.of(zeroScoreUser));

        given(quizCacheService.getQuizForGame(QUIZ_NAME))
                .willReturn(createTestQuiz());
    }

    @Test
    void givenLoggedUserAndTwoQuestions_whenSubmittingAllCorrectAnswers_thenShouldSaveAndReturnFullScore()
            throws Exception {


        MvcResult startResult = mockMvc.perform(
                        post("/game/start")
                                .header("X-KEY", TEST_KEY)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                              "quizName": "Difficulty",
                              "size": 2,
                              "difficulty": "EASY"
                            }
                            """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.question").isNotEmpty())
                .andReturn();

        GameEntry startedGame = readGameEntry(startResult);

        assertThat(startedGame.id()).isNotNull();
        assertThat(startedGame.question())
                .isNotBlank()
                .startsWith("Question 1:");

        UUID gameId = startedGame.id();

        String firstAnswer =
                getCorrectAnswer(startedGame.question());

        MvcResult firstAnswerResult =
                sendAnswer(TEST_KEY, gameId, firstAnswer);

        GameEntry gameAfterFirstAnswer =
                readGameEntry(firstAnswerResult);

        assertThat(gameAfterFirstAnswer.id())
                .isEqualTo(gameId);

        assertThat(gameAfterFirstAnswer.question())
                .isNotBlank()
                .startsWith("Question 2:")
                .isNotEqualTo(startedGame.question());

        String secondAnswer =
                getCorrectAnswer(gameAfterFirstAnswer.question());

        MvcResult secondAnswerResult =
                sendAnswer(TEST_KEY, gameId, secondAnswer);

        QuizResultDTO finishedResult =
                readQuizResult(secondAnswerResult);

        assertThat(finishedResult.id())
                .isEqualTo(gameId.toString());

        assertThat(finishedResult.score())
                .isEqualTo(2);

        assertThat(finishedResult.questions())
                .hasSize(2);

        assertThat(finishedResult.answers())
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        "Warsaw",
                        "4"
                );

        MvcResult getResult = mockMvc.perform(
                        get("/game/result/{id}", finishedResult.id())
                                .header("X-KEY", TEST_KEY)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(finishedResult.id()))
                .andExpect(jsonPath("$.score")
                        .value(2))
                .andExpect(jsonPath("$.answers.length()")
                        .value(2))
                .andReturn();

        QuizResultDTO retrievedResult =
                readQuizResult(getResult);

        assertThat(retrievedResult.id())
                .isEqualTo(finishedResult.id());

        assertThat(retrievedResult.score())
                .isEqualTo(finishedResult.score());

        assertThat(retrievedResult.answers())
                .isEqualTo(finishedResult.answers());

        assertThat(retrievedResult.questions())
                .hasSameSizeAs(finishedResult.questions());

        verify(userService, times(4))
                .getByKey(TEST_KEY);

        verify(quizCacheService)
                .getQuizForGame(QUIZ_NAME);
    }

    @Test
    void shouldFinishGameWithZeroScoreWhenAllAnswersAreWrong()
            throws Exception {

        MvcResult startResult = mockMvc.perform(
                        post("/game/start")
                                .header("X-KEY", ZERO_SCORE_KEY)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                              "quizName": "Difficulty",
                              "size": 2,
                              "difficulty": "EASY"
                            }
                            """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.question").isNotEmpty())
                .andReturn();

        GameEntry startedGame = readGameEntry(startResult);

        assertThat(startedGame.id()).isNotNull();
        assertThat(startedGame.question())
                .isNotBlank()
                .startsWith("Question 1:");

        UUID gameId = startedGame.id();

        String firstWrongAnswer =
                getWrongAnswer(startedGame.question());

        MvcResult firstAnswerResult =
                sendAnswer(ZERO_SCORE_KEY, gameId, firstWrongAnswer);

        GameEntry gameAfterFirstAnswer =
                readGameEntry(firstAnswerResult);

        assertThat(gameAfterFirstAnswer.id())
                .isEqualTo(gameId);

        assertThat(gameAfterFirstAnswer.question())
                .isNotBlank()
                .startsWith("Question 2:")
                .isNotEqualTo(startedGame.question());


        String secondWrongAnswer =
                getWrongAnswer(gameAfterFirstAnswer.question());

        MvcResult secondAnswerResult =
                sendAnswer(ZERO_SCORE_KEY, gameId, secondWrongAnswer);

        QuizResultDTO finishedResult =
                readQuizResult(secondAnswerResult);

        assertThat(finishedResult.id())
                .isEqualTo(gameId.toString());

        assertThat(finishedResult.score())
                .isZero();

        assertThat(finishedResult.questions())
                .hasSize(2);

        assertThat(finishedResult.answers())
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        "Berlin",
                        "5"
                );

        MvcResult getResult = mockMvc.perform(
                        get("/game/result/{id}", finishedResult.id())
                                .header("X-KEY", ZERO_SCORE_KEY)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(finishedResult.id()))
                .andExpect(jsonPath("$.score")
                        .value(0))
                .andExpect(jsonPath("$.answers.length()")
                        .value(2))
                .andReturn();

        QuizResultDTO retrievedResult =
                readQuizResult(getResult);

        assertThat(retrievedResult.id())
                .isEqualTo(finishedResult.id());

        assertThat(retrievedResult.score())
                .isEqualTo(finishedResult.score());

        assertThat(retrievedResult.answers())
                .isEqualTo(finishedResult.answers());

        assertThat(retrievedResult.questions())
                .hasSameSizeAs(finishedResult.questions());

        verify(userService, times(4))
                .getByKey(ZERO_SCORE_KEY);

        verify(quizCacheService)
                .getQuizForGame(QUIZ_NAME);
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test-user");
        user.setEmail("test@email.com");
        return user;
    }

    private User createZeroScoreUser() {
        User user = new User();
        user.setId(2L);
        user.setUsername("zero-score-user");
        user.setEmail("zero-score@email.com");
        return user;
    }

    private static final Map<String, String> CORRECT_ANSWERS = Map.of(
            "Capital city of Poland?", "Warsaw",
            "2 + 2 = ?", "4"
    );

    private static final Map<String, String> WRONG_ANSWERS = Map.of(
            "Capital city of Poland?", "Berlin",
            "2 + 2 = ?", "5"
    );

    private GameEntry readGameEntry(MvcResult mvcResult)
            throws Exception {

        return objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                GameEntry.class
        );
    }

    private String getCorrectAnswer(String questionMessage) {
        return CORRECT_ANSWERS.entrySet()
                .stream()
                .filter(entry -> questionMessage.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError(
                                "Unknown question: " + questionMessage
                        )
                );
    }

    private String getWrongAnswer(String questionMessage) {
        return WRONG_ANSWERS.entrySet()
                .stream()
                .filter(entry -> questionMessage.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError(
                                "Unknown question: " + questionMessage
                        )
                );
    }

    private MvcResult sendAnswer(String userKey, UUID gameId, String answer)
            throws Exception {

        AnswerInput answerInput = new AnswerInput(
                gameId,
                answer
        );

        return mockMvc.perform(
                        post("/game/answer")
                                .header("X-KEY", userKey)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(answerInput))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(gameId.toString()))
                .andReturn();
    }

    private QuizResultDTO readQuizResult(MvcResult mvcResult)
            throws Exception {

        return objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                QuizResultDTO.class
        );
    }

    private Quiz createTestQuiz() {
        Question firstQuestion = new Question(
                "Capital city of Poland?",
                "Warsaw"
        );
        firstQuestion.setId(1L);
        firstQuestion.setDifficulty(Difficulty.EASY);

        Question secondQuestion = new Question(
                "2 + 2 = ?",
                "4"
        );
        secondQuestion.setId(2L);
        secondQuestion.setDifficulty(Difficulty.EASY);

        Quiz quiz = new Quiz();
        quiz.setId(1L);
        quiz.setTitle(QUIZ_NAME);
        quiz.setQuestions(
                new LinkedHashSet<>(
                        List.of(firstQuestion, secondQuestion)
                )
        );

        return quiz;
    }
}
