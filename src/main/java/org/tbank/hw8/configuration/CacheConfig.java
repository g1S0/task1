package org.tbank.hw8.configuration;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Objects;

@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("currencyRate");
    }

    @Scheduled(fixedRateString = "${currency-api.cbr-api.cache-time}")
    public void evictAllCachesAtIntervals() {
        final Cache cache = cacheManager().getCache("currencyRate");
        if (cache == null) {
            return;
        }
        cache.clear();
    }
}