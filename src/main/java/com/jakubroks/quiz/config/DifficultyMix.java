package com.jakubroks.quiz.config;

import com.jakubroks.quiz.entity.Difficulty;

import java.util.Map;

public class DifficultyMix {
    private final Map<Difficulty, Integer> map;

    public DifficultyMix(int easy, int medium, int hard) {
        if (easy < 0 || medium < 0 || hard < 0 || (easy + medium + hard) == 0) {
            throw new IllegalArgumentException("Invalid mix: " + easy + ":" + medium + ":" + hard);
        }
        this.map = Map.of(
            Difficulty.EASY, easy,
            Difficulty.MEDIUM, medium,
            Difficulty.HARD, hard
        );
    }

    public int get(Difficulty difficulty) {
        return map.getOrDefault(difficulty, 0);
    }

    public Map<Difficulty, Integer> asMap() {
        return map;
    }
}
