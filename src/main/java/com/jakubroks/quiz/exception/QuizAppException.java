package com.jakubroks.quiz.exception;

public abstract class QuizAppException extends RuntimeException {
    public QuizAppException(String message) {
        super(message);
    }
}
