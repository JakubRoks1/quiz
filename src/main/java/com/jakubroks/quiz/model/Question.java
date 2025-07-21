package com.jakubroks.quiz.model;

import lombok.Data;

@Data
public class Question {
    private Long id;
    private String text;
    private String correctAnswer;
}
