package com.jakubroks.quiz.config;

import com.jakubroks.quiz.entity.Difficulty;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.val;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// to możesz wywalić czyli podmienić z QuizDifficultyPropertiesConfigurationNew (podmień logikę i zostaw nazwę) [x]
// przejrzyj kod programu bo wydaje mi się że nie możesz grać w quizy mixed (i wybierać poziomów pośrednich)
// w nowej logice (tej z New) zrób tak że jak nie podamy defaultmix to żeby był pierwszy z konfiguracji z mixes [x]
// mozesz pomyslec o uproszeczniu parsedRatiosPerMix (*czyli dodanie wlasnej nowej klasy - czyli zeby nie bylo mapy w mapie) [x]
@Data
@ConfigurationProperties(prefix = "quiz.difficulty")
public class QuizDifficultyPropertiesConfiguration {

    private final Map<String, String> mixes;
    private final String defaultMix;

    private final Map<String, DifficultyMix> parsedRatiosPerMix;

    @ConstructorBinding
    public QuizDifficultyPropertiesConfiguration(Map<String, String> mixes,
                                                    String defaultMix) {

        if (mixes == null || mixes.isEmpty()) {
            throw new IllegalStateException("No quiz difficulty mixes defined");
        }

        this.mixes = Collections.unmodifiableMap(mixes);

        String resolvedDefaultMix = defaultMix;
        if (resolvedDefaultMix == null || resolvedDefaultMix.isBlank() ||  !mixes.containsKey(resolvedDefaultMix)) {
            resolvedDefaultMix = mixes.keySet().iterator().next();
        }
        this.defaultMix = resolvedDefaultMix;

        val ratios = new HashMap<String, DifficultyMix>();
        for (var entry : mixes.entrySet()) {
            var result = parseRatio(entry.getValue());
            ratios.put(entry.getKey(), result);
        }
        this.parsedRatiosPerMix = Collections.unmodifiableMap(ratios);



        System.out.println("xxxxxx");
        System.out.println(mixes);
        System.out.println(this.defaultMix);
        System.out.println(ratios);
        System.out.println("xxxxxx");

    }

    public Map<Difficulty, Integer> getByMix(String mixName) {
        DifficultyMix mix = parsedRatiosPerMix.get(mixName);
        return mix != null ? mix.asMap() : null;
    }


    private DifficultyMix parseRatio(String ratio) {
        try {
            String[] parts = ratio.split(":");
            if (parts.length != 3) throw new IllegalArgumentException();

            int easy = Integer.parseInt(parts[0]);
            int medium = Integer.parseInt(parts[1]);
            int hard = Integer.parseInt(parts[2]);

            return new DifficultyMix(easy, medium, hard);

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid difficulty ratio: " + ratio, e);
        }
    }

}
