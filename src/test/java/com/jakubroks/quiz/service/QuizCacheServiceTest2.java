package com.jakubroks.quiz.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.jakubroks.quiz.entity.Quiz;
import com.jakubroks.quiz.repository.QuizRepository;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class QuizCacheServiceTest2 {

    @MockitoBean
    private QuizRepository quizRepository;

    @MockitoSpyBean
    private QuizCacheService quizCacheService;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void clearCache() {
        cacheManager.resetCaches();
    }

    @Test
    void givenQuizName_whenGetQuizForGame_thenShouldPutQuizIntoCache() {

        Mockito.when(quizRepository.findByTitle("Mario")).thenReturn(Optional.of(new Quiz()));

        quizCacheService.getQuizForGame("Mario");

        Mockito.verify(quizRepository).findByTitle("Mario");

        quizCacheService.getQuizForGame("Mario");
        quizCacheService.getQuizForGame("Mario");

        Mockito.verifyNoMoreInteractions(quizRepository);
    }

    @Test
    void givenMoreThanThreeQuizzes_whenGetQuizForGame_thenShouldNotStoreMoreThanThreeQuizzes() throws InterruptedException {
        Mockito.when(quizRepository.findByTitle(Mockito.anyString())).thenReturn(Optional.of(new Quiz()));

        quizCacheService.getQuizForGame("Science");
        quizCacheService.getQuizForGame("Geography");
        quizCacheService.getQuizForGame("Difficulty");
        quizCacheService.getQuizForGame("Quiz");

        Mockito.verify(quizRepository, Mockito.times(4)).findByTitle(Mockito.anyString());

        quizCacheService.getQuizForGame("Quiz");
        quizCacheService.getQuizForGame("Quiz");

        Mockito.verify(quizRepository, Mockito.times(4)).findByTitle(Mockito.anyString());

        quizCacheService.getQuizForGame("Difficulty");

        Mockito.verify(quizRepository, Mockito.times(5)).findByTitle(Mockito.anyString());
    }
}
