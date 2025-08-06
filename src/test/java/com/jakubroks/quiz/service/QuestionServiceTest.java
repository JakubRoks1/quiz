package com.jakubroks.quiz.service;

import com.jakubroks.quiz.dto.QuestionDTO;
import com.jakubroks.quiz.entity.Question;
import com.jakubroks.quiz.repository.QuestionRepository;
import com.jakubroks.quiz.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QuestionServiceTest {

    private QuestionRepository questionRepository;
    private QuizRepository quizRepository;
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        questionRepository = mock(QuestionRepository.class);
        quizRepository = mock(QuizRepository.class);
        questionService = new QuestionService(questionRepository, quizRepository);
    }

    @Test
    void givenValidQuestionDTO_whenAddQuestion_thenShouldReturnAddedQuestion() {
        QuestionDTO dto = new QuestionDTO("What is the capital of France?", "Paris");
        Question question = new Question();
        question.setText(dto.text());
        question.setCorrectAnswer(dto.correctAnswer());
        when(questionRepository.save(any())).thenReturn(question);

        Question result = questionService.addQuestion(dto);

        assertThat(result.getText()).isEqualTo(dto.text());
    }

    @Test
    void givenExistingQuestionId_whenDeleteQuestion_thenShouldDeleteQuestion() {
        Question question = new Question();
        question.setId(1L);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        doNothing().when(questionRepository).deleteById(1L);

        questionService.deleteQuestion(1L);

        verify(questionRepository).deleteById(1L);
    }

    @Test
    void givenExistingQuestionAndValidDTO_whenUpdateQuestion_thenShouldUpdateAndReturnQuestion() {
        Question question = new Question();
        question.setId(1L);
        question.setText("Old text");
        QuestionDTO dto = new QuestionDTO("New text", "A");
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(questionRepository.save(any())).thenReturn(question);

        Question result = questionService.updateQuestion(1L, dto);

        assertThat(result.getText()).isEqualTo(dto.text());
        verify(questionRepository).findById(1L);
        verify(questionRepository).save(question);
    }
}
