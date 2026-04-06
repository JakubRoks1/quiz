package com.jakubroks.quiz.calculator;

import com.jakubroks.quiz.entity.Difficulty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FixedOrderSkippingZeroWeightDistributionCalculatorTest {

    private FixedOrderSkippingZeroWeightDistributionCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new FixedOrderSkippingZeroWeightDistributionCalculator();
    }

    @Test
    void givenZeroMediumWeight_whenCalculating_thenSkipsMediumInRemainingDistribution() {
        Map<Difficulty, Integer> weights = Map.of(
                Difficulty.EASY, 1,
                Difficulty.MEDIUM, 0,
                Difficulty.HARD, 1
        );

        Map<Difficulty, Integer> result = calculator.calculate(3, weights);

        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                Difficulty.EASY, 2,
                Difficulty.MEDIUM, 0,
                Difficulty.HARD, 1
        ));
    }

    @Test
    void givenZeroEasyWeight_whenCalculating_thenEasyGetsZeroQuestions() {
        Map<Difficulty, Integer> weights = Map.of(
                Difficulty.EASY, 0,
                Difficulty.MEDIUM, 1,
                Difficulty.HARD, 1
        );

        Map<Difficulty, Integer> result = calculator.calculate(3, weights);

        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                Difficulty.EASY, 0,
                Difficulty.MEDIUM, 2,
                Difficulty.HARD, 1
        ));
    }
}
