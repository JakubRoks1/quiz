package com.jakubroks.quiz;

import com.jakubroks.quiz.entity.Question;
import com.jakubroks.quiz.entity.Quiz;
import lombok.Data;
import lombok.Getter;

import java.util.*;

@Getter
@Data
public final class PendingGame {
    private final UUID id;
    private final Quiz quiz;
    private List<Question> questions;
    private final List<String> answers = new ArrayList<>();

    public PendingGame(UUID id, Quiz quiz, List<Question> questions) {
        this.id = id;
        this.quiz = quiz;
        this.questions = questions;
    }

    public UUID getId() {
        return id;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public List<Question> getQuestions() {
        return questions;
    }


    public boolean submitAnswer(String answer) {
        answers.add(answer);
        return answers.size() < questions.size();
    }

    public int getScore() {
        int score = 0;
        for (int i = 0; i < answers.size() && i < questions.size(); i++) {
            if (answers.get(i).equalsIgnoreCase(questions.get(i).getCorrectAnswer())) {
                score++;
            }
        }
        return score;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PendingGame) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.quiz, that.quiz) &&
                Objects.equals(this.questions, that.questions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quiz, questions);
    }

    @Override
    public String toString() {
        return "PendingGame[" +
                "id=" + id + ", " +
                "quiz=" + quiz + ", " +
                "questions=" + questions + ']';
    }
}
