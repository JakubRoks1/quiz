package com.jakubroks.quiz.service;


import com.jakubroks.quiz.entity.Quiz;
import com.jakubroks.quiz.repository.QuizRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QuizCacheService {
    private final QuizRepository quizRepository;

    public QuizCacheService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Cacheable(cacheNames = "cache", key = "#quizName")
    public Quiz getQuizForGame(String quizName) {
        log.info("Getting quiz for game: {} to have fun!", quizName.toUpperCase());

        return quizRepository.findByTitle(quizName)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
    }
}
