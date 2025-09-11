package com.jakubroks.quiz.dto;

import java.io.Serializable;
import java.util.List;

public record QuizResultDTO (
        String id,
        List<QuestionDTO> questions,
        List<String> answers,
        int score
) implements Serializable {

}
