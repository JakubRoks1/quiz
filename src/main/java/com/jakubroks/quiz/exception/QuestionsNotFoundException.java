package com.jakubroks.quiz.exception;

public class QuestionsNotFoundException extends QuizAppException {
    public static final String MESSAGE = "No questions available";

    public QuestionsNotFoundException() {
        super(MESSAGE);
    }

    public QuestionsNotFoundException(String message) {
        super(message);
    }
}
