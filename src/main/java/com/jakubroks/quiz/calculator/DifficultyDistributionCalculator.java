package com.jakubroks.quiz.calculator;

import com.jakubroks.quiz.entity.Difficulty;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DifficultyDistributionCalculator {

    public Map<Difficulty, Integer> calculate(int total, Map<Difficulty, Integer> weights) {

        int weightSum = weights.values().stream().mapToInt(Integer::intValue).sum();

        return weights.entrySet().stream()
                .map(entry -> {
                    double value = (double) total * entry.getValue() / weightSum;
                    return Map.entry(entry.getKey(), (int) Math.round(value));
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // sprawdzic (przypadek 1:1:1 - 4 pytania) jak nie przydzielone pytania to randomowo dorzuc dowolnie

    }

    public Map<Difficulty, Integer> calculateUsingFixedOrder(int total, Map<Difficulty, Integer> weights) {
        validateTotal(total);
        validateWeights(weights);

        Map<Difficulty, Integer> result = calculateBaseDistributionWithFloor(total, weights);
        int remaining = total - countAssignedQuestions(result);

        for (Difficulty difficulty : List.of(Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD)) {
            if (remaining == 0) {
                break;
            }

            if (result.containsKey(difficulty)) {
                result.put(difficulty, result.get(difficulty) + 1);
                remaining--;
            }
        }

        return result;
    }

    public Map<Difficulty, Integer> calculateUsingReversedOrder(int total, Map<Difficulty, Integer> weights) {
        validateTotal(total);
        validateWeights(weights);

        Map<Difficulty, Integer> result = calculateBaseDistributionWithFloor(total, weights);
        int remaining = total - countAssignedQuestions(result);

        for (Difficulty difficulty : List.of(Difficulty.HARD, Difficulty.MEDIUM, Difficulty.EASY)) {
            if (remaining == 0) {
                break;
            }

            if (result.containsKey(difficulty)) {
                result.put(difficulty, result.get(difficulty) + 1);
                remaining--;
            }
        }

        return result;
    }

    public Map<Difficulty, Integer> calculateUsingFixedOrderSkippingZeroWeight(int total, Map<Difficulty, Integer> weights) {
        validateTotal(total);
        validateWeights(weights);

        Map<Difficulty, Integer> result = calculateBaseDistributionWithFloor(total, weights);
        int remaining = total - countAssignedQuestions(result);

        for (Difficulty difficulty : List.of(Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD)) {
            if (remaining == 0) {
                break;
            }

            if (result.containsKey(difficulty) && weights.getOrDefault(difficulty, 0) > 0) {
                result.put(difficulty, result.get(difficulty) + 1);
                remaining--;
            }
        }

        return result;
    }



    private Map<Difficulty, Integer> calculateBaseDistributionWithFloor(int total, Map<Difficulty, Integer> weights) {
        int weightSum = sumWeights(weights);
        Map<Difficulty, Integer> result = new EnumMap<>(Difficulty.class);

        for (Map.Entry<Difficulty, Integer> entry : weights.entrySet()) {
            int value = (int) Math.floor((double) total * entry.getValue() / weightSum);
            result.put(entry.getKey(), value);
        }

        return result;
    }

    private int countAssignedQuestions(Map<Difficulty, Integer> distribution) {
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

    private void validateWeights(Map<Difficulty, Integer> weights) {
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
