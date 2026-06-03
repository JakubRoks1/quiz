package com.jakubroks.quiz.service;


import com.jakubroks.quiz.entity.Quiz;
import com.jakubroks.quiz.repository.QuizRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class QuizCacheService {

    private final QuizRepository quizRepository;

    public QuizCacheService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Cacheable(cacheNames = "cache", key = "#quizName")
    public Quiz getQuizForGame(String quizName) {
        System.out.println("Getting quiz for game: " + quizName);

        return quizRepository.findByTitle(quizName)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
    }
}
