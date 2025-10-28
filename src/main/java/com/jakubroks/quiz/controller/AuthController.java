package com.jakubroks.quiz.controller;

import com.jakubroks.quiz.dto.AuthKey;
import com.jakubroks.quiz.dto.ChangePasswordRequest;
import com.jakubroks.quiz.dto.LoginRequest;
import com.jakubroks.quiz.dto.RegisterRequest;
import com.jakubroks.quiz.entity.User;
import com.jakubroks.quiz.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody RegisterRequest req) {
        return userService.register(req);
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthKey> auth(@RequestBody LoginRequest req) {
        String authKey = userService.authenticate(req.email(), req.password());
        if (authKey == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(new AuthKey(authKey));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("X-KEY") String key) {
        return userService.logout(key)
                ? ResponseEntity.ok(Map.of("message", "Logged out successfully"))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid or expired key"));
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@RequestBody ChangePasswordRequest req) {
        userService.resetPassword(req.email(), req.oldPassword(), req.newPassword());
    }


}

