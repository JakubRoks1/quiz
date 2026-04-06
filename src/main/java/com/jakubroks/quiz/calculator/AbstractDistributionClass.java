package com.jakubroks.quiz.calculator;

import com.jakubroks.quiz.entity.Difficulty;

import java.util.EnumMap;
import java.util.Map;

public abstract class AbstractDistributionClass implements DistributionCalculator {

    protected Map<Difficulty, Integer> calculateBaseDistributionWithFloor(int total, Map<Difficulty, Integer> weights) {
        int weightSum = sumWeights(weights);
        Map<Difficulty, Integer> result = new EnumMap<>(Difficulty.class);

        for (Map.Entry<Difficulty, Integer> entry : weights.entrySet()) {
            int value = (int) Math.floor((double) total * entry.getValue() / weightSum);
            result.put(entry.getKey(), value);
        }

        return result;
    }

    protected int countAssignedQuestions(Map<Difficulty, Integer> distribution) {
        return distribution.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    void validateTotal(int total) {
        if (total < 1) {
            throw new IllegalArgumentException("Total must be greater than 0");
        }
    }

    private int sumWeights(Map<Difficulty, Integer> weights) {
        return weights.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    protected void validateWeights(Map<Difficulty, Integer> weights) {
        if (weights == null || weights.isEmpty()) {
            throw new IllegalArgumentException("Weights cannot be null or empty");
        }

        int sum = 0;

        for (Map.Entry<Difficulty, Integer> entry : weights.entrySet()) {
            if (entry.getKey() == null) {
                throw new IllegalArgumentException("Difficulty cannot be null");
            }

            if (entry.getValue() == null) {
                throw new IllegalArgumentException("Weight cannot be null");
            }

            if (entry.getValue() < 0) {
                throw new IllegalArgumentException("Weights cannot be negative");
            }

            if (entry.getKey() == Difficulty.MIXED) {
                throw new IllegalArgumentException("MIXED is not supported");
            }

            sum += entry.getValue();
        }

        if (sum == 0) {
            throw new IllegalArgumentException("At least one weight must be greater than 0");
        }
    }
}
