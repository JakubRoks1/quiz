package com.jakubroks.quiz.config;

import com.jakubroks.quiz.security.PasswordHasher;
import com.jakubroks.quiz.security.SaltPasswordHasher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SecurityConfig {

    @Bean
    public PasswordHasher passwordHasher(
            @Value("${app.security.simple-salt:ConstantSól123}") String salt) {
        return new SaltPasswordHasher(salt);
    }
}
