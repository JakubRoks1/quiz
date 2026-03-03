package com.jakubroks.quiz.picker;

import com.jakubroks.quiz.calculator.DifficultyDistributionCalculator;
import com.jakubroks.quiz.config.QuizDifficultyPropertiesConfiguration;
import com.jakubroks.quiz.entity.Difficulty;
import com.jakubroks.quiz.entity.Question;
import com.jakubroks.quiz.entity.Quiz;
import com.jakubroks.quiz.exception.QuestionsNotFoundException;
import com.jakubroks.quiz.exception.TooManyQuestionsRequestedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class QuestionPicker {

    private final DifficultyDistributionCalculator distributionCalculator;
    private final QuizDifficultyPropertiesConfiguration difficultyProperties;

    public QuestionPicker(DifficultyDistributionCalculator distributionCalculator, QuizDifficultyPropertiesConfiguration difficultyProperties) {
        this.distributionCalculator = distributionCalculator;
        this.difficultyProperties = difficultyProperties;
    }

    public List<Question> pick(Quiz quiz, int numberOfQuestions, String mixName) {

        if (quiz.getQuestions() == null || quiz.getQuestions().isEmpty()) {
            throw new QuestionsNotFoundException();
        }

        Map<Difficulty, Integer> difficultyWeights = difficultyProperties.getByMix(mixName); // tutaj

        Map<Difficulty, Integer> targetCountsByDifficulty = distributionCalculator.calculate(numberOfQuestions, difficultyWeights);

        Map<Difficulty, List<Question>> questionsByDifficulty = quiz.getQuestions().stream()
                    .collect(Collectors.groupingBy(Question::getDifficulty));

        List<Question> selectedQuestions = new ArrayList<>(numberOfQuestions);

        for (var entry : targetCountsByDifficulty.entrySet()) {
            Difficulty diff = entry.getKey();
            int requiredCount = entry.getValue();

            List<Question> questions = new ArrayList<>(questionsByDifficulty.getOrDefault(diff, List.of()));

            if (questions.size() < requiredCount) {
                throw new TooManyQuestionsRequestedException(
                        "Not enough questions for difficulty " + diff
                );
            }

            Collections.shuffle(questions);
            selectedQuestions.addAll(questions.stream().limit(requiredCount).toList());
        }

        Collections.shuffle(selectedQuestions);
        return selectedQuestions;
    }

}
