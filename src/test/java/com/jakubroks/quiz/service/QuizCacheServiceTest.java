package com.jakubroks.quiz.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class QuizCacheServiceTest {

    @Autowired
    private QuizCacheService quizCacheService;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void clearCache() {
        cacheManager.getCache("cache").clear();
    }

    @Test
    void givenQuizName_whenGetQuizForGame_thenShouldPutQuizIntoCache() {
        quizCacheService.getQuizForGame("Science");

        assertNotNull(cacheManager.getCache("cache").get("Science"));
    }

    @Test
    void givenMoreThanThreeQuizzes_whenGetQuizForGame_thenShouldNotStoreMoreThanThreeQuizzes() {
        quizCacheService.getQuizForGame("Science");
        quizCacheService.getQuizForGame("Geography");
        quizCacheService.getQuizForGame("Difficulty");
        quizCacheService.getQuizForGame("Quiz");

        com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache =
                (com.github.benmanes.caffeine.cache.Cache<Object, Object>)
                        cacheManager.getCache("cache").getNativeCache();

        nativeCache.cleanUp();

        assertTrue(nativeCache.asMap().size() <= 3);
    }
}
