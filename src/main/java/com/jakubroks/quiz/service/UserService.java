package com.jakubroks.quiz.service;

import com.jakubroks.quiz.dto.RegisterRequest;
import com.jakubroks.quiz.entity.LoggedUsersMap;
import com.jakubroks.quiz.entity.User;
import com.jakubroks.quiz.repository.UserRepository;
import com.jakubroks.quiz.security.PasswordHasher;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final LoggedUsersMap loggedUsersMap;

    public UserService(UserRepository userRepository, PasswordHasher passwordHasher, LoggedUsersMap loggedUsersMap) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.loggedUsersMap = loggedUsersMap;
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

    public String authenticate(String email, String rawPassword) {
        Optional<User> user = userRepository.findByEmail(email)
                .filter(u -> passwordHasher.matches(rawPassword, u.getPassword()));
        if (user.isEmpty()) {
            return null;
        }

        String authKey = UUID.randomUUID().toString();
        loggedUsersMap.addUser(authKey, user.get());
        return authKey;
    }

    public boolean logout(String key) {
        User existing = loggedUsersMap.getLoggedUsers().get(key);
        if (existing == null) return false;

        loggedUsersMap.removeUserByKey(key);
        return true;
    }

    public Optional<User> getByKey(String key) {
        return Optional.ofNullable(loggedUsersMap.getLoggedUsers().get(key));
    }
}
