package com.jakubroks.quiz.calculator;

import com.jakubroks.quiz.entity.Difficulty;
import org.springframework.stereotype.Component;

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
}
