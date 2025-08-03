package com.jakubroks.quiz.exception;

public class GameAlreadyStartedException extends QuizAppException {
    public static final String MESSAGE = "Game has already started.";

    public GameAlreadyStartedException() {
        super(MESSAGE);
    }

    public GameAlreadyStartedException(String message) {
        super(message);
    }
}
