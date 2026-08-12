package com.jakubroks.quiz.integration;

import com.jakubroks.quiz.controller.ReportController;
import com.jakubroks.quiz.dto.QuestionDTO;
import com.jakubroks.quiz.dto.QuizResultDTO;
import com.jakubroks.quiz.entity.User;
import com.jakubroks.quiz.service.GameService;
import com.jakubroks.quiz.service.ReportService;
import com.jakubroks.quiz.service.UserService;
import com.jakubroks.quiz.service.report.PdfReportGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
@Import({
        ReportService.class,
        PdfReportGenerator.class
})
class ReportFlowIntegrationTest {
    private static final String TEST_KEY = "test-key";
    private static final Path REPORTS_DIRECTORY = Path.of("reports");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @MockitoBean
    private UserService userService;

    @BeforeEach
    void setUp() throws IOException {
        User testUser = createTestUser();

        Files.createDirectories(REPORTS_DIRECTORY);

        given(userService.getByKey(TEST_KEY))
                .willReturn(Optional.of(testUser));
    }

    @Test
    void givenExistingGame_whenGettingReport_thenShouldReturnPdfAndCreateFile() throws Exception {

        String gameId = "integration-test-report";

        QuizResultDTO quizResult =
                createFinishedGameResult(gameId);

        given(gameService.getFinishedGame(gameId))
                .willReturn(quizResult);

        Path expectedReportFile = getExpectedReportPath(gameId);

        Files.deleteIfExists(expectedReportFile);

        try {
            MvcResult mvcResult = mockMvc.perform(
                            get("/report/{id}", gameId)
                                    .header("X-KEY", TEST_KEY)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/pdf"))
                    .andReturn();

            byte[] responseBytes =
                    mvcResult.getResponse().getContentAsByteArray();

            assertThat(responseBytes.length).isGreaterThan(100);

            assertThat(Files.size(expectedReportFile)).isGreaterThan(0);

            byte[] savedFileBytes =
                    Files.readAllBytes(expectedReportFile);

            assertThat(savedFileBytes).isEqualTo(responseBytes);

            verify(userService).getByKey(TEST_KEY);
            verify(gameService).getFinishedGame(gameId);
        } finally {
            Files.deleteIfExists(expectedReportFile);
        }
    }

    @Test
    void givenNonExistingGame_whenGettingReport_thenShouldReturn404AndNotCreateFile() throws Exception {

        String nonExistingGameId = "non-existing-game-id";

        given(gameService.getFinishedGame(nonExistingGameId))
                .willReturn(null);

        Path expectedReportFile =
                getExpectedReportPath(nonExistingGameId);

        Files.deleteIfExists(expectedReportFile);

        mockMvc.perform(
                        get("/report/{id}", nonExistingGameId)
                                .header("X-KEY", TEST_KEY)
                )
                .andExpect(status().isNotFound());

        assertThat(Files.exists(expectedReportFile)).isFalse();

        verify(gameService).getFinishedGame(nonExistingGameId);
        verify(userService).getByKey(TEST_KEY);


    }

    private static QuizResultDTO createFinishedGameResult(String gameId) {
        List<QuestionDTO> questions = List.of(
                new QuestionDTO(
                        "Capital city of Poland?",
                        "Warsaw"
                ),
                new QuestionDTO(
                        "2 + 2 = ?",
                        "4"
                )

        );

        List<String> answers = List.of(
                "Warsaw",
                "4"
        );

        return new QuizResultDTO(
                gameId,
                questions,
                answers,
                2
        );

    }

    private static User createTestUser() {
        User user = new User();
        user.setUsername("test-user");
        user.setEmail("test@email.com");
        return user;
    }

    private Path getExpectedReportPath(String gameId) {
        return REPORTS_DIRECTORY.resolve(
                "quiz_report_" + gameId + ".pdf"
        );
    }


}
