package com.jakubroks.quiz.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({QuizDifficultyPropertiesConfiguration.class, QuizDifficultyPropertiesConfigurationNew.class})
public class AppConfig {
}
