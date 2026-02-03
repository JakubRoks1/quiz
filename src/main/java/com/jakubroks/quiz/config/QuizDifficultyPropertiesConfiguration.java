package com.jakubroks.quiz.config;

import com.jakubroks.quiz.entity.Difficulty;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "quiz.difficulty")
public class QuizDifficultyPropertiesConfiguration {

    private Map<String, String> mixes = new HashMap<>();
    private String defaultMix;

    @PostConstruct
    void validate() {
        if (defaultMix == null || !mixes.containsKey(defaultMix)) {
            throw new IllegalStateException("Default quiz difficulty mix is not defined");
        }

        String ratio = mixes.get(defaultMix);
        parseRatio(ratio);
    }

    public Map<Difficulty, Integer> getDefaultMixAsWeights() {
        return parseRatio(mixes.get(defaultMix));
    }

    private Map<Difficulty, Integer> parseRatio(String ratio) {
        try {
            String[] parts = ratio.split(":");
            if (parts.length != 3) {
                throw new IllegalArgumentException();
            }

            int easy = Integer.parseInt(parts[0]);
            int medium = Integer.parseInt(parts[1]);
            int hard = Integer.parseInt(parts[2]);

            if (easy < 0 || medium < 0 || hard < 0 || (easy + medium + hard) == 0) {
                throw new IllegalArgumentException();
            }

            return Map.of(
                    Difficulty.EASY, easy,
                    Difficulty.MEDIUM, medium,
                    Difficulty.HARD, hard
            );
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid difficulty ratio: " + ratio,
                    e
            );
        }

    }
}
