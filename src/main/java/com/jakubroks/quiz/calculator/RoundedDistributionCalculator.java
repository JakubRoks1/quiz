package com.jakubroks.quiz.calculator;

import com.jakubroks.quiz.entity.Difficulty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "calculator.type", havingValue = "rounded")
public class RoundedDistributionCalculator extends AbstractDistributionClass {

    @Override
    public Map<Difficulty, Integer> calculate(int total, Map<Difficulty, Integer> weights) {

        int weightSum = weights.values().stream().mapToInt(Integer::intValue).sum();

        return weights.entrySet().stream()
                .map(entry -> {
                    double value = (double) total * entry.getValue() / weightSum;
                    return Map.entry(entry.getKey(), (int) Math.round(value));
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

}
