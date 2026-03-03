package com.jakubroks.quiz.picker;

import com.jakubroks.quiz.calculator.DifficultyDistributionCalculator;
import com.jakubroks.quiz.config.QuizDifficultyPropertiesConfiguration;
import com.jakubroks.quiz.entity.Difficulty;
import com.jakubroks.quiz.entity.Question;
import com.jakubroks.quiz.entity.Quiz;
import com.jakubroks.quiz.exception.QuestionsNotFoundException;
import com.jakubroks.quiz.exception.TooManyQuestionsRequestedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.jakubroks.quiz.entity.Difficulty.EASY;
import static com.jakubroks.quiz.entity.Difficulty.MEDIUM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionPickerTest {

    @Mock
    DifficultyDistributionCalculator calculator;
    @Mock
    QuizDifficultyPropertiesConfiguration properties;

    @InjectMocks
    QuestionPicker picker;

    @Test
    void givenValidQuestionPicker_whenPick_thenShouldReturnQuestions() {

        Quiz quiz = quiz(
                question(1L, EASY),
                question(2L, EASY),
                question(3L, MEDIUM)
        );

        int size = 2;
        String mixName = "MIX";

        Map<Difficulty, Integer> weights = Map.of(EASY, 50, MEDIUM, 50);
        Map<Difficulty, Integer> target  = Map.of(EASY, 1, MEDIUM, 1);

        when(properties.getByMix(mixName)).thenReturn(weights);
        when(calculator.calculate(size, weights)).thenReturn(target);

        List<Question> result = picker.pick(quiz, size, mixName);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Question::getDifficulty)
                .containsExactlyInAnyOrder(EASY, MEDIUM);
    }

    @Test
    void givenNotEnoughEasyQuestions_whenPick_thenShouldThrowException() {

        Quiz quiz = quiz(
                question(1L, EASY),
                question(2L, EASY)
        );

        int size = 3;
        String mixName = "MIX";

        Map<Difficulty, Integer> weights = Map.of(EASY, 100);

        Map<Difficulty, Integer> target  = Map.of(EASY, 3);

        when(properties.getByMix(mixName)).thenReturn(weights);
        when(calculator.calculate(size, weights)).thenReturn(target);

        assertThatThrownBy(() -> picker.pick(quiz, size, mixName))
                .isInstanceOf(TooManyQuestionsRequestedException.class)
                .hasMessageContaining("Not enough questions for difficulty EASY");
    }

    @Test
    void givenNoQuestions_whenPick_thenShouldThrowException() {

        Quiz quiz = quiz();
        quiz.setQuestions(new HashSet<>());

        assertThatThrownBy(() -> picker.pick(quiz, 1, "MIX"))
                .isInstanceOf(QuestionsNotFoundException.class);

        verifyNoInteractions(properties, calculator);
    }

    private Question question(Long id, Difficulty difficulty) {
        Question q = new Question();
        q.setId(id);
        q.setDifficulty(difficulty);
        return q;
    }

    private Quiz quiz(Question... questions) {
        Quiz quiz = new Quiz();
        quiz.setQuestions(new HashSet<>(List.of(questions)));
        return quiz;
    }

    }











