package com.jakubroks.quiz.calculator;

import com.jakubroks.quiz.entity.Difficulty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FixedOrderDistributionCalculatorTest {

    private FixedOrderDistributionCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new FixedOrderDistributionCalculator();
    }

    @Test
    void givenWeights_whenCalculating_thenReturnsDistribution() {
        Map<Difficulty, Integer> weights = Map.of(
                Difficulty.EASY, 50,
                Difficulty.MEDIUM, 30,
                Difficulty.HARD, 20
        );

        Map<Difficulty, Integer> result = calculator.calculate(7, weights);

        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                Difficulty.EASY, 4,
                Difficulty.MEDIUM, 2,
                Difficulty.HARD, 1
        ));
    }

    @Test
    void givenSameInput_whenCalculatingTwice_thenReturnsSameResult() {
        Map<Difficulty, Integer> weights = Map.of(
                Difficulty.EASY, 50,
                Difficulty.MEDIUM, 30,
                Difficulty.HARD, 20
        );

        Map<Difficulty, Integer> firstResult = calculator.calculate(7, weights);
        Map<Difficulty, Integer> secondResult = calculator.calculate(7, weights);

        assertThat(firstResult).isEqualTo(secondResult);
    }

    @Test
    void givenZeroTotal_whenCalculating_thenThrowsIllegalArgumentException() {
        Map<Difficulty, Integer> weights = Map.of(
                Difficulty.EASY, 50,
                Difficulty.MEDIUM, 30,
                Difficulty.HARD, 20
        );

        assertThatThrownBy(() -> calculator.calculate(0, weights))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Total must be greater than 0");
    }

    @Test
    void givenNullWeights_whenCalculating_thenThrowsIllegalArgumentException() {
        assertThatThrownBy(() -> calculator.calculate(5, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Weights cannot be null or empty");
    }

    @Test
    void givenEmptyWeights_whenCalculating_thenThrowsIllegalArgumentException() {
        assertThatThrownBy(() -> calculator.calculate(5, Map.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Weights cannot be null or empty");
    }

    @Test
    void givenAllWeightsEqualZero_whenCalculating_thenThrowsIllegalArgumentException() {
        Map<Difficulty, Integer> weights = Map.of(
                Difficulty.EASY, 0,
                Difficulty.MEDIUM, 0,
                Difficulty.HARD, 0
        );

        assertThatThrownBy(() -> calculator.calculate(5, weights))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("At least one weight must be greater than 0");
    }

    @Test
    void givenNegativeWeight_whenCalculating_thenThrowsIllegalArgumentException() {
        Map<Difficulty, Integer> weights = Map.of(
                Difficulty.EASY, -1,
                Difficulty.MEDIUM, 1,
                Difficulty.HARD, 1
        );

        assertThatThrownBy(() -> calculator.calculate(5, weights))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Weights cannot be negative");
    }

    @Test
    void givenMixedDifficultyWeight_whenCalculating_thenThrowsIllegalArgumentException() {
        Map<Difficulty, Integer> weights = Map.of(
                Difficulty.EASY, 1,
                Difficulty.MIXED, 1,
                Difficulty.HARD, 1
        );

        assertThatThrownBy(() -> calculator.calculate(5, weights))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("MIXED is not supported");
    }
}
