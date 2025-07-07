package com.example.quiz.entry;

import com.example.quiz.entity.Question;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GameEntry(UUID id, String question, List<Question> questions, List<String> answers, Integer score) {
    public GameEntry(UUID id, String question) {
        this(id, question, null, null, null);
    }

    public GameEntry(UUID id, List<Question> questions, List<String> answers, Integer score) {
        this(id, null, questions, answers, score);
    }
}
