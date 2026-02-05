package com.jakubroks.quiz.config;

import com.jakubroks.quiz.entity.Difficulty;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

// to możesz wywalić czyli podmienić z QuizDifficultyPropertiesConfigurationNew (podmień logikę i zostaw nazwę)
// przejrzyj kod programu bo wydaje mi się że nie możesz grać w quizy mixed (i wybierać poziomów pośrednich)
// w nowej logice (tej z New) zrób tak że jak nie podamy defaultmix to żeby był pierwszy z konfiguracji z mixes
// mozesz pomyslec o uproszeczniu parsedRatiosPerMix (*czyli dodanie wlasnej nowej klasy - czyli zeby nie bylo mapy w mapie)
@Data
@ConfigurationProperties(prefix = "quiz.difficulty")
public class QuizDifficultyPropertiesConfiguration {

    private Map<String, String> mixes = new HashMap<>();
    private String defaultMix;

    @PostConstruct
    void validate() {
        if (defaultMix == null || !mixes.containsKey(defaultMix)) {
            throw new IllegalStateException("Default quiz difficulty mix is not defined");
        }
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
