package com.jakubroks.quiz.exception;

public class TooManyQuestionsRequestedException extends QuizAppException {
    public static final String MESSAGE = "Requested number of questions exceeds available questions.";

    public TooManyQuestionsRequestedException() {
        super(MESSAGE);
    }

    public TooManyQuestionsRequestedException(String message) {
        super(message);
    }
}