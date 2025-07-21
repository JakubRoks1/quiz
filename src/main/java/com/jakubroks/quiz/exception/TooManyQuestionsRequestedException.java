package com.jakubroks.quiz.exception;

public class TooManyQuestionsRequestedException extends RuntimeException {
    public TooManyQuestionsRequestedException(String message) {
        super(message);
    }
}
