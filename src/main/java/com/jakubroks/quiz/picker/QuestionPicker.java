package com.jakubroks.quiz.picker;

import com.jakubroks.quiz.calculator.DistributionCalculator;
import com.jakubroks.quiz.config.QuizDifficultyPropertiesConfiguration;
import com.jakubroks.quiz.entity.Difficulty;
import com.jakubroks.quiz.entity.Question;
import com.jakubroks.quiz.entity.Quiz;
import com.jakubroks.quiz.exception.QuestionsNotFoundException;
import com.jakubroks.quiz.exception.TooManyQuestionsRequestedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class QuestionPicker {

    private final DistributionCalculator distributionCalculator;
    private final QuizDifficultyPropertiesConfiguration difficultyProperties;


    @Autowired
    public QuestionPicker(DistributionCalculator difficultyDistributionCalculator2,
                          QuizDifficultyPropertiesConfiguration difficultyProperties) {
        System.out.println(difficultyDistributionCalculator2.getClass().getName());
        this.distributionCalculator = difficultyDistributionCalculator2;
        this.difficultyProperties = difficultyProperties;
    }
//
//    @Autowired
//    public QuestionPicker(List<DistributionCalculator> distributionCalculator,
//                          QuizDifficultyPropertiesConfiguration difficultyProperties) {
//        System.out.println(distributionCalculator.getClass().getName());
//        this.distributionCalculator = distributionCalculator.get(0);
//        this.difficultyProperties = difficultyProperties;
//    }

//    @Autowired(required = false)
//    public QuestionPicker(Optional<DifficultyDistributionCalculator> difficultyDistributionCalculator,
//                          Optional<DifficultyDistributionCalculator2> difficultyDistributionCalculator2,
//                          QuizDifficultyPropertiesConfiguration difficultyProperties) {
//        System.out.println("c1");
//        if (difficultyDistributionCalculator.isPresent()) {
//            this.distributionCalculator = difficultyDistributionCalculator.get();
//        } else {
//            this.distributionCalculator = difficultyDistributionCalculator2.get();
//        }
//
//        this.difficultyProperties = difficultyProperties;
//    }
//    @Autowired(required = false)
//    public QuestionPicker(DifficultyDistributionCalculator2 difficultyDistributionCalculator2,
//                          QuizDifficultyPropertiesConfiguration difficultyProperties) {
//        System.out.println("c2");
//        this.distributionCalculator = difficultyDistributionCalculator2;
//        this.difficultyProperties = difficultyProperties;
//    }
//
//    @Autowired(required = false)
//    public QuestionPicker(DifficultyDistributionCalculator difficultyDistributionCalculator,
//                          QuizDifficultyPropertiesConfiguration difficultyProperties) {
//        System.out.println("c3");
//        this.distributionCalculator = difficultyDistributionCalculator;
//        this.difficultyProperties = difficultyProperties;
//    }
//
//    @Autowired(required = false)
//    public QuestionPicker(QuizDifficultyPropertiesConfiguration difficultyProperties) {
//        System.out.println("x");
//        this.distributionCalculator = null;
//        this.difficultyProperties = difficultyProperties;
//    }

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
