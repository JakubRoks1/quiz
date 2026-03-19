package com.jakubroks.quiz.calculator;

import com.jakubroks.quiz.entity.Difficulty;

import java.util.Map;

public interface DistributionCalculator {

    Map<Difficulty, Integer> calculate(int total, Map<Difficulty, Integer> weights);
}
