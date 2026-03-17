package com.jakubroks.quiz.calculator;

import com.jakubroks.quiz.entity.Difficulty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;


class DifficultyDistributionCalculatorTest {

    private DifficultyDistributionCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new DifficultyDistributionCalculator();
    }

    @Test
    void givenTotal6AndRatio132_whenCalculatingDistribution_thenSplitIsExact() {
        int total = 6;


        Map<Difficulty, Integer> ratio = Map.of(
                Difficulty.EASY, 1,
                Difficulty.MEDIUM, 3,
                Difficulty.HARD, 2
        );


        Map<Difficulty, Integer> result = calculator.calculate(total, ratio);


        assertEquals(1, result.get(Difficulty.EASY));
        assertEquals(3, result.get(Difficulty.MEDIUM));
        assertEquals(2, result.get(Difficulty.HARD));


        int sum = result.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();


        assertEquals(6, sum);
    }


    @Test
    void givenTotal8AndRatio132_whenCalculatingDistribution_thenRoundingIsApplied() {
        int total = 8;


        Map<Difficulty, Integer> ratio = Map.of(
                Difficulty.EASY, 1,
                Difficulty.MEDIUM, 3,
                Difficulty.HARD, 2
        );


        Map<Difficulty, Integer> result = calculator.calculate(total, ratio);


        int sum = result.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();


        assertEquals(8, sum);


        assertTrue(result.get(Difficulty.MEDIUM) > result.get(Difficulty.EASY));
        assertTrue(result.get(Difficulty.HARD) >= 2);
    }


    @Test
    void givenTotal7AndRatio132_whenCalculatingDistribution_thenSumEqualsTotal() {
        int total = 7;


        Map<Difficulty, Integer> ratio = Map.of(
                Difficulty.EASY, 1,
                Difficulty.MEDIUM, 3,
                Difficulty.HARD, 2
        );


        Map<Difficulty, Integer> result = calculator.calculate(total, ratio);


        int sum = result.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();


        assertEquals(7, sum);
    }


    @Test
    void givenZeroEasyWeight_whenCalculatingDistribution_thenEasyGetsZeroQuestions() {
        int total = 6;


        Map<Difficulty, Integer> ratio = Map.of(
                Difficulty.EASY, 0,
                Difficulty.MEDIUM, 2,
                Difficulty.HARD, 4
        );


        Map<Difficulty, Integer> result = calculator.calculate(total, ratio);


        assertEquals(0, result.get(Difficulty.EASY));


        int sum = result.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();


        assertEquals(6, sum);
    }

    @Test
    void givenWeights_whenCalculatingUsingFixedOrder_thenReturnsDistribution() {
        Map<Difficulty, Integer> weights = Map.of(
                Difficulty.EASY, 50,
                Difficulty.MEDIUM, 30,
                Difficulty.HARD, 20
        );

        Map<Difficulty, Integer> result = calculator.calculateUsingFixedOrder(7, weights);

        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                Difficulty.EASY, 4,
                Difficulty.MEDIUM, 2,
                Difficulty.HARD, 1
        ));
    }

    @Test
    void givenWeights_whenCalculatingUsingReversedOrder_thenReturnsDistribution() {
        Map<Difficulty, Integer> weights = Map.of(
                Difficulty.EASY, 50,
                Difficulty.MEDIUM, 30,
                Difficulty.HARD, 20
        );

        Map<Difficulty, Integer> result = calculator.calculateUsingReversedOrder(7, weights);

        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                Difficulty.EASY, 3,
                Difficulty.MEDIUM, 2,
                Difficulty.HARD, 2
        ));
    }

    @Test
    void givenZeroMediumWeight_whenCalculatingUsingFixedOrderSkippingZeroWeight_thenSkipsMediumInRemainingDistribution() {
        Map<Difficulty, Integer> weights = Map.of(
                Difficulty.EASY, 1,
                Difficulty.MEDIUM, 0,
                Difficulty.HARD, 1
        );

        Map<Difficulty, Integer> result = calculator.calculateUsingFixedOrderSkippingZeroWeight(3, weights);

        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                Difficulty.EASY, 2,
                Difficulty.MEDIUM, 0,
                Difficulty.HARD, 1
        ));
    }

    @Test
    void givenZeroEasyWeight_whenCalculatingUsingFixedOrderSkippingZeroWeight_thenEasyGetsZeroQuestions() {
        Map<Difficulty, Integer> weights = Map.of(
                Difficulty.EASY, 0,
                Difficulty.MEDIUM, 1,
                Difficulty.HARD, 1
        );

        Map<Difficulty, Integer> result = calculator.calculateUsingFixedOrderSkippingZeroWeight(3, weights);

        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
                Difficulty.EASY, 0,
                Difficulty.MEDIUM, 2,
                Difficulty.HARD, 1
        ));
    }

    @Test
    void givenSameInput_whenCalculatingUsingFixedOrderTwice_thenReturnsSameResult() {
        Map<Difficulty, Integer> weights = Map.of(
                Difficulty.EASY, 50,
                Difficulty.MEDIUM, 30,
                Difficulty.HARD, 20
        );

        Map<Difficulty, Integer> firstResult = calculator.calculateUsingFixedOrder(7, weights);
        Map<Difficulty, Integer> secondResult = calculator.calculateUsingFixedOrder(7, weights);

        assertThat(firstResult).isEqualTo(secondResult);
    }

    @Test
    void givenZeroTotal_whenCalculatingUsingFixedOrder_thenThrowsIllegalArgumentException() {
        Map<Difficulty, Integer> weights = Map.of(
                Difficulty.EASY, 50,
                Difficulty.MEDIUM, 30,
                Difficulty.HARD, 20
        );

        assertThatThrownBy(() -> calculator.calculateUsingFixedOrder(0, weights))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Total must be greater than 0");
    }

    @Test
    void givenNullWeights_whenCalculatingUsingFixedOrder_thenThrowsIllegalArgumentException() {
        assertThatThrownBy(() -> calculator.calculateUsingFixedOrder(5, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Weights cannot be null or empty");
    }

    @Test
    void givenEmptyWeights_whenCalculatingUsingFixedOrder_thenThrowsIllegalArgumentException() {
        assertThatThrownBy(() -> calculator.calculateUsingFixedOrder(5, Map.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Weights cannot be null or empty");
    }

    @Test
    void givenAllWeightsEqualZero_whenCalculatingUsingFixedOrder_thenThrowsIllegalArgumentException() {
        Map<Difficulty, Integer> weights = Map.of(
                Difficulty.EASY, 0,
                Difficulty.MEDIUM, 0,
                Difficulty.HARD, 0
        );

        assertThatThrownBy(() -> calculator.calculateUsingFixedOrder(5, weights))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("At least one weight must be greater than 0");
    }

    @Test
    void givenNegativeWeight_whenCalculatingUsingFixedOrder_thenThrowsIllegalArgumentException() {
        Map<Difficulty, Integer> weights = Map.of(
                Difficulty.EASY, -1,
                Difficulty.MEDIUM, 1,
                Difficulty.HARD, 1
        );

        assertThatThrownBy(() -> calculator.calculateUsingFixedOrder(5, weights))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Weights cannot be negative");
    }

    @Test
    void givenMixedDifficultyWeight_whenCalculatingUsingFixedOrder_thenThrowsIllegalArgumentException() {
        Map<Difficulty, Integer> weights = Map.of(
                Difficulty.EASY, 1,
                Difficulty.MIXED, 1,
                Difficulty.HARD, 1
        );

        assertThatThrownBy(() -> calculator.calculateUsingFixedOrder(5, weights))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("MIXED is not supported");
    }
}

