package com.jakubroks.quiz.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableCaching
@Slf4j
//@ConditionalOnProperty(name = "spring.cache.type", havingValue = "caffeine")
public class CaffeineCacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("cache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(3));
        log.info("Caffeine cache manager created");
        return cacheManager;
    }
}
