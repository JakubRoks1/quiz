package com.jakubroks.quiz.config;

import com.jakubroks.quiz.entity.Difficulty;

import java.util.Map;

public record DifficultyMix(int easy, int medium, int hard) {

    public DifficultyMix {
        if (easy < 0 || medium < 0 || hard < 0 || (easy + medium + hard) == 0) {
            throw new IllegalArgumentException("Invalid mix: " + easy + ":" + medium + ":" + hard);
        }
    }

    public int get(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> easy;
            case MEDIUM -> medium;
            case HARD -> hard;
            default -> 0;
        };
    }

    public Map<Difficulty, Integer> asMap() {
        return Map.of(
                Difficulty.EASY, easy,
                Difficulty.MEDIUM, medium,
                Difficulty.HARD, hard
        );
    }
}
