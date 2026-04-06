package com.jakubroks.quiz.calculator;

import com.jakubroks.quiz.entity.Difficulty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ReversedOrderDistributionCalculatorTest {

    private ReversedOrderDistributionCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new ReversedOrderDistributionCalculator();
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
                Difficulty.EASY, 3,
                Difficulty.MEDIUM, 2,
                Difficulty.HARD, 2
        ));
    }
}
