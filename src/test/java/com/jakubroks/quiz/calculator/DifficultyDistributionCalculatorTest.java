package com.jakubroks.quiz.calculator;

import com.jakubroks.quiz.entity.Difficulty;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class DifficultyDistributionCalculatorTest {

    private final DifficultyDistributionCalculator calculator =
            new DifficultyDistributionCalculator();

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
}

