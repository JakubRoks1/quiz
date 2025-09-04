package com.jakubroks.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public record QuizResultDTO(
        String id,
        List<QuestionDTO> questions,
        List<String> answers,
        int score
) {

}
