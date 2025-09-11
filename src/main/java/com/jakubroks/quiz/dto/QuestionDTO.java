package com.jakubroks.quiz.dto;

import java.io.Serializable;

public record QuestionDTO(String text, String correctAnswer) implements Serializable {
}
