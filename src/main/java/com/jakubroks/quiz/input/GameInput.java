package com.jakubroks.quiz.input;

import com.jakubroks.quiz.entity.Difficulty;

public record GameInput(String quizName, int size, Difficulty difficulty) {

}

