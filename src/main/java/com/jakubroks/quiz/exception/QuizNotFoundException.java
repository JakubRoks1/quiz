package com.jakubroks.quiz.exception;

public class QuizNotFoundException extends QuizAppException {
    public static final String MESSAGE = "Quiz not found.";

    public QuizNotFoundException() {
        super(MESSAGE);
    }

    public QuizNotFoundException(String message) {
        super(message);
    }
}
