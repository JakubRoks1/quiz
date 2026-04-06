package com.jakubroks.quiz.calculator;

import com.jakubroks.quiz.entity.Difficulty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "calculator.type", havingValue = "reversed-order")
public class ReversedOrderDistributionCalculator extends AbstractDistributionClass {

    @Override
    public Map<Difficulty, Integer> calculate(int total, Map<Difficulty, Integer> weights) {
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
}
