package com.jakubroks.quiz.dto;

public record ChangePasswordRequest(String email, String oldPassword, String newPassword) {}

