package com.jakubroks.quiz.controller;

import com.jakubroks.quiz.filter.AuthFilter;
import com.jakubroks.quiz.entity.Quiz;
import com.jakubroks.quiz.picker.QuestionPicker;
import com.jakubroks.quiz.service.AuthService;
import com.jakubroks.quiz.service.QuizService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(QuizController.class)
class QuizControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @MockitoBean
    private QuizService quizService;

    @MockitoBean
    private QuestionPicker questionPicker;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private AuthFilter authFilter;

    @Test
    void givenQuizzesExist_whenGettingAllQuizzes_thenReturnsQuizList() {
        Quiz quiz = new Quiz();
        quiz.setId(1L);
        quiz.setTitle("Java quiz");

        when(quizService.getAllQuizzes()).thenReturn(List.of(quiz));

        assertThat(mockMvc.get().uri("/quizzes"))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[0].id").isEqualTo(1);

        assertThat(mockMvc.get().uri("/quizzes"))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[0].title").isEqualTo("Java quiz");
    }

    @Test
    void givenExistingQuizId_whenGettingQuizById_thenReturnsQuiz() {
        Quiz quiz = new Quiz();
        quiz.setId(1L);
        quiz.setTitle("Spring quiz");

        when(quizService.getQuiz(1L)).thenReturn(quiz);

        assertThat(mockMvc.get().uri("/quizzes/1"))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.id").isEqualTo(1);

        assertThat(mockMvc.get().uri("/quizzes/1"))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.title").isEqualTo("Spring quiz");
    }

    @Test
    void givenValidQuizDto_whenCreatingQuiz_thenReturnsCreatedQuiz() {
        Quiz createdQuiz = new Quiz();
        createdQuiz.setId(1L);
        createdQuiz.setTitle("New quiz");

        when(quizService.addQuiz(any())).thenReturn(createdQuiz);

        assertThat(mockMvc.post().uri("/quizzes")
                .contentType("application/json")
                .content("""
                    {
                      "title": "New quiz"
                    }
                    """))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.title").isEqualTo("New quiz");

        verify(quizService).addQuiz(any());
    }

    @Test
    void givenExistingQuizId_whenDeletingQuiz_thenDeletesQuiz() {
        assertThat(mockMvc.delete().uri("/quizzes/1"))
                .hasStatusOk();

        verify(quizService).deleteQuiz(1L);
    }

    @Test
    void givenExistingQuizAndValidDto_whenUpdatingQuiz_thenReturnsUpdatedQuiz() {
        Quiz updatedQuiz = new Quiz();
        updatedQuiz.setId(1L);
        updatedQuiz.setTitle("Updated quiz");

        when(quizService.updateQuiz(eq(1L), any())).thenReturn(updatedQuiz);

        assertThat(mockMvc.put().uri("/quizzes/1")
                .contentType("application/json")
                .content("""
                    {
                      "title": "Updated quiz"
                    }
                    """))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.title").isEqualTo("Updated quiz");

        verify(quizService).updateQuiz(eq(1L), any());
    }
}
