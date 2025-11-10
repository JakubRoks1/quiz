package com.jakubroks.quiz.service;

import com.jakubroks.quiz.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

@Service
public class AuthService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public ResponseEntity<Object> requireLogin(String key, Function<User, ResponseEntity<Object>> ok) {
        if (key == null || key.isBlank()) {
            return ResponseEntity.status(401).body(Map.of("message","Missing X-KEY"));
        }

        return userService.getByKey(key)
                .map(ok)
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("message","Invalid or expired key")));
    }
}
