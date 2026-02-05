package com.jakubroks.quiz.config;

import com.jakubroks.quiz.entity.Difficulty;
import lombok.Data;
import lombok.val;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "quiz.difficulty")
public class QuizDifficultyPropertiesConfigurationNew {

    private final Map<String, String> mixes;
    private final String defaultMix;

    private final Map<String, Map<Difficulty, Integer>> parsedRatiosPerMix;

    @ConstructorBinding
    public QuizDifficultyPropertiesConfigurationNew(Map<String, String> mixes,
                                                    String defaultMix) {
        this.mixes = Collections.unmodifiableMap(mixes);
        this.defaultMix = defaultMix;

        if (defaultMix == null || !mixes.containsKey(defaultMix)) {
            throw new IllegalStateException("Default quiz difficulty mix is not defined");
        }

        val ratios = new HashMap<String, Map<Difficulty, Integer>>();
        for (var entry : mixes.entrySet()) {
            var result = parseRatio(entry.getValue());
            ratios.put(entry.getKey(), result);
        }
        this.parsedRatiosPerMix = Collections.unmodifiableMap(ratios);


        System.out.println("xxxxxx");
        System.out.println(mixes);
        System.out.println(defaultMix);
        System.out.println(ratios);
        System.out.println("xxxxxx");

    }

    public Map<Difficulty, Integer> getByMix(String mixName) {
        return parsedRatiosPerMix.get(mixName);
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
