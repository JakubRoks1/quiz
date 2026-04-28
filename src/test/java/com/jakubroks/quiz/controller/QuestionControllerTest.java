package com.jakubroks.quiz.controller;

import com.jakubroks.quiz.dto.QuestionDTO;
import com.jakubroks.quiz.entity.Question;
import com.jakubroks.quiz.filter.AuthFilter;
import com.jakubroks.quiz.service.AuthService;
import com.jakubroks.quiz.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @MockitoBean
    private QuestionService questionService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private AuthFilter authFilter;

    @Test
    void givenValidQuestionDto_whenCreatingQuestion_thenReturnsCreatedQuestion() {
        Question question = new Question();
        question.setId(1L);
        question.setText("What is the third planet from the Sun?");
        question.setCorrectAnswer("Earth");

        when(questionService.addQuestion(any(QuestionDTO.class))).thenReturn(question);

        assertThat(mockMvc.post().uri("/questions")
                .contentType("application/json")
                .content("""
            {
              "text": "What is the third planet from the Sun?",
              "correctAnswer": "Earth"
            }
            """))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.correctAnswer").isEqualTo("Earth");

        verify(questionService).addQuestion(any(QuestionDTO.class));
    }

    @Test
    void givenExistingQuestionAndValidDto_whenUpdatingQuestion_thenReturnsUpdatedQuestion() {
        Question question = new Question();
        question.setId(1L);
        question.setText("Capital city of Poland?");
        question.setCorrectAnswer("Warsaw");

        when(questionService.updateQuestion(eq(1L), any(QuestionDTO.class))).thenReturn(question);

        assertThat(mockMvc.put().uri("/questions/1")
                .contentType("application/json")
                .content("""
                    {
                      "text": "Capital city of Poland?",
                      "correctAnswer": "Warsaw"
                    }
                    """))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.correctAnswer").isEqualTo("Warsaw");

        verify(questionService).updateQuestion(eq(1L), any(QuestionDTO.class));
    }

    @Test
    void givenExistingQuestionId_whenDeletingQuestion_thenDeletesQuestion() {
        assertThat(mockMvc.delete().uri("/questions/1"))
                .hasStatusOk();

        verify(questionService).deleteQuestion(1L);
    }



}
