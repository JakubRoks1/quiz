package com.jakubroks.quiz.exception;

public class GameNotFoundException extends QuizAppException {
    public static final String MESSAGE = "Game not found.";

    public GameNotFoundException() {
        super(MESSAGE);
    }

    public GameNotFoundException(String message) {
        super(message);
    }
}