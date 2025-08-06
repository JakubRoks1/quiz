package com.jakubroks.quiz.service;

import com.jakubroks.quiz.controller.QuizController;
import com.jakubroks.quiz.dto.QuizDTO;
import com.jakubroks.quiz.exception.QuizNotFoundException;
import com.jakubroks.quiz.entity.Quiz;
import com.jakubroks.quiz.repository.QuestionRepository;
import com.jakubroks.quiz.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuizServiceTest {

    private QuizRepository quizRepository;
    private QuestionRepository questionRepository;
    private QuizService quizService;

    @BeforeEach
    void setUp() {
        quizRepository = mock(QuizRepository.class);
        questionRepository = mock(QuestionRepository.class);
        quizService = new QuizService(quizRepository, questionRepository);
    }

    @Test
    void givenValidQuizDTO_whenAddQuiz_thenShouldSaveAndReturnQuiz() {
        QuizDTO dto = new QuizDTO("Sample Quiz");
        Quiz quiz = new Quiz();
        quiz.setTitle("Sample Quiz");
        when(quizRepository.save(any(Quiz.class))).thenReturn(quiz);

        Quiz result = quizService.addQuiz(dto);

        assertThat(result.getTitle()).isEqualTo("Sample Quiz");
        verify(quizRepository).save(any(Quiz.class));
    }

    @Test
    void givenQuizId_whenDeleteQuiz_thenShouldDeleteQuizById() {
        quizService.deleteQuiz(123L);
        verify(quizRepository).deleteById(123L);
    }

    @Test
    void givenQuizzesInRepository_whenGetAllQuizzes_thenShouldReturnAllQuizzes() {
        List<Quiz> quizzes = List.of(new Quiz(), new Quiz());
        when(quizRepository.findAll()).thenReturn(quizzes);

        List<Quiz> result = quizService.getAllQuizzes();

        assertThat(result).hasSize(2);
        verify(quizRepository).findAll();
    }

    @Test
    void givenExistingQuizId_whenGetQuizById_thenShouldReturnQuiz() {
        Quiz quiz = new Quiz();
        quiz.setTitle("Sample");
        when(quizRepository.findById(33L)).thenReturn(Optional.of(quiz));

        Quiz result = quizService.getQuiz(33L);

        assertThat(result).isSameAs(quiz);
        verify(quizRepository).findById(33L);
    }

    @Test
    void givenNonExistingQuizId_whenGetQuizById_thenShouldThrowNoSuchElementException() {
        when(quizRepository.findById(55L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> quizService.getQuiz(55L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void givenExistingQuizAndValidDto_whenUpdateQuiz_thenShouldUpdateAndReturnQuiz() {
        Long quizId = 1L;
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setTitle("Old title");
        QuizController.QuizDto dto = new QuizController.QuizDto(quizId, "New title");

        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));
        when(quizRepository.save(any(Quiz.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Quiz updatedQuiz = quizService.updateQuiz(quizId, dto);

        assertThat(updatedQuiz.getTitle()).isEqualTo("New title");
        verify(quizRepository).findById(quizId);
        verify(quizRepository).save(quiz);
    }
}
