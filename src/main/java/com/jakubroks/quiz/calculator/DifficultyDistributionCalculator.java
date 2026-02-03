package com.jakubroks.quiz.calculator;

import com.jakubroks.quiz.entity.Difficulty;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class DifficultyDistributionCalculator {

    public Map<Difficulty, Integer> calculate(int total, Map<Difficulty, Integer> weights) {

        int weightSum = weights.values().stream().mapToInt(Integer::intValue).sum();

        Map<Difficulty, Double> raw = new EnumMap<>(Difficulty.class);
        Map<Difficulty, Integer> result = new EnumMap<>(Difficulty.class);

        int assigned = 0;

        for (var e : weights.entrySet()) {
            double value = (double) total * e.getValue() / weightSum;
            int floor = (int) Math.floor(value);

            raw.put(e.getKey(), value);
            result.put(e.getKey(), floor);
            assigned += floor;
        }

        int remaining = total - assigned;

        List<Difficulty> order = List.of(Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD);

        order.stream()
                .sorted((a, b) -> Double.compare(
                        raw.get(b) - result.get(b),
                        raw.get(a) - result.get(a)
                ))
                .limit(remaining)
                .forEach(d -> result.put(d, result.get(d) + 1));


        return result;
    }
}
