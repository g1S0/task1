package org.tbank.hw5.configuration;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;


@Configuration
@EnableScheduling
@Slf4j
public class ExecutorConfig {
    @Value("${fixed.thread.pool.size}")
    private int fixedThreadPoolSize;

    @Value("${scheduled.thread.pool.size}")
    private int scheduledThreadPoolSize;

    @Bean(name = "dataLoaderThreadPool")
    public ExecutorService dataLoaderThreadPool() {
        log.info("Creating fixed thread pool with size: {}", fixedThreadPoolSize);

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("dataLoaderThreadPool-Worker-%d")
                .build();

        ExecutorService executorService = Executors.newFixedThreadPool(fixedThreadPoolSize, namedThreadFactory);
        log.info("Fixed thread pool created successfully");
        return executorService;
    }

    @Bean(name = "scheduledThreadPool")
    public ScheduledExecutorService scheduledThreadPool() {
        log.info("Creating scheduled thread pool with size: {}", scheduledThreadPoolSize);

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("ScheduledPool-Worker-%d")
                .build();

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(scheduledThreadPoolSize, namedThreadFactory);
        log.info("Scheduled thread pool created successfully");
        return scheduledExecutorService;
    }

}
