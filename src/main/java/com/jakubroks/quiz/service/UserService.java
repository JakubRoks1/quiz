package com.jakubroks.quiz.service;

import com.jakubroks.quiz.dto.RegisterRequest;
import com.jakubroks.quiz.entity.User;
import com.jakubroks.quiz.repository.UserRepository;
import com.jakubroks.quiz.security.PasswordHasher;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        String hashed = passwordHasher.hash(request.password());
        user.setPassword(hashed);

        return userRepository.save(user);
    }

    public boolean authenticate(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .map(u -> passwordHasher.matches(rawPassword, u.getPassword()))
                .orElse(false);
    }

    @Transactional
    public void resetPassword(String email, String oldPassword, String newPassword) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordHasher.matches(oldPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        if (oldPassword.equals(newPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must differ from old password");
        }

        user.setPassword(passwordHasher.hash(newPassword));
        userRepository.save(user);
    }
}
