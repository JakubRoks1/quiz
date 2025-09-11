package com.jakubroks.quiz.controller;

import com.jakubroks.quiz.dto.ChangePasswordRequest;
import com.jakubroks.quiz.dto.LoginRequest;
import com.jakubroks.quiz.dto.RegisterRequest;
import com.jakubroks.quiz.entity.User;
import com.jakubroks.quiz.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    public boolean auth(@RequestBody LoginRequest req) {
        return userService.authenticate(req.email(), req.password());
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@RequestBody ChangePasswordRequest req) {
        userService.resetPassword(req.email(), req.oldPassword(), req.newPassword());
    }
}

