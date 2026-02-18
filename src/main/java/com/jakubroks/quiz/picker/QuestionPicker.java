package com.jakubroks.quiz.picker;

import com.jakubroks.quiz.calculator.DifficultyDistributionCalculator;
import com.jakubroks.quiz.config.QuizDifficultyPropertiesConfiguration;
import com.jakubroks.quiz.entity.Difficulty;
import com.jakubroks.quiz.entity.Question;
import com.jakubroks.quiz.entity.Quiz;
import com.jakubroks.quiz.exception.TooManyQuestionsRequestedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class QuestionPicker {

    private final DifficultyDistributionCalculator calculator;
    private final QuizDifficultyPropertiesConfiguration properties;

    public QuestionPicker(DifficultyDistributionCalculator calculator, QuizDifficultyPropertiesConfiguration properties) {
        this.calculator = calculator;
        this.properties = properties;
    }

    public List<Question> pick(Quiz quiz, int size) {

        Map<Difficulty, Integer> weights = properties.getByMix("MIXED");

        Map<Difficulty, Integer> target = calculator.calculate(size, weights);

        Map<Difficulty, List<Question>> pool = quiz.getQuestions().stream()
                    .collect(Collectors.groupingBy(Question::getDifficulty));

        List<Question> result = new ArrayList<>(size);

        for (var entry : target.entrySet()) {
            Difficulty diff = entry.getKey();
            int count = entry.getValue();

            List<Question> questions = new ArrayList<>(pool.getOrDefault(diff, List.of()));

            if (questions.size() < count) {
                throw new TooManyQuestionsRequestedException(
                        "Not enough questions for difficulty " + diff
                );
            }

            Collections.shuffle(questions);
            result.addAll(questions.stream().limit(count).toList());
        }

        Collections.shuffle(result);
        return result;
    }

}
