package com.jakubroks.quiz.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.util.concurrent.TimeUnit;

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
    void givenMoreThanThreeQuizzes_whenGetQuizForGame_thenShouldNotStoreMoreThanThreeQuizzes() throws InterruptedException {
        quizCacheService.getQuizForGame("Science");
        quizCacheService.getQuizForGame("Geography");
        quizCacheService.getQuizForGame("Difficulty");
        quizCacheService.getQuizForGame("Quiz");

        Cache<Object, Object> nativeCache =
                (Cache<Object, Object>)
                        cacheManager.getCache("cache").getNativeCache();

        Awaitility.await().atMost(4, TimeUnit.SECONDS)
            .pollInterval(500, TimeUnit.MILLISECONDS)
            .until(() -> nativeCache.asMap().size() <= 3);
//        nativeCache.cleanUp();

        assertTrue(nativeCache.asMap().size() <= 3);
    }
}
