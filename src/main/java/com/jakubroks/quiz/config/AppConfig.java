package com.jakubroks.quiz.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({QuizDifficultyPropertiesConfiguration.class, QuizDifficultyPropertiesConfiguration.class})
public class AppConfig {
}
