package org.tbank.hw5.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


@Configuration
@EnableScheduling
@Slf4j
public class ExecutorConfig {
    @Value("${fixed.thread.pool.size}")
    private int fixedThreadPoolSize;

    @Value("${scheduled.thread.pool.size}")
    private int scheduledThreadPoolSize;

    @Bean(name = "dataLoaderThreadPool")
    public ExecutorService fixedThreadPool() {
        log.info("Creating fixed thread pool with size: {}", fixedThreadPoolSize);

        ThreadFactory namedThreadFactory = new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            private final String threadName = "FixedPool-Worker-";

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName(threadName + threadNumber.getAndIncrement());
                log.info("Created new thread: {}", thread.getName());
                return thread;
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(fixedThreadPoolSize, namedThreadFactory);
        log.info("Fixed thread pool created successfully");
        return executorService;
    }

    @Bean(name = "scheduledThreadPool")
    public ScheduledExecutorService scheduledThreadPool() {
        log.info("Creating scheduled thread pool with size: {}", scheduledThreadPoolSize);

        ThreadFactory namedThreadFactory = new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            private final String threadName = "ScheduledPool-Worker-";

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName(threadName + threadNumber.getAndIncrement());
                log.info("Created new thread: {}", thread.getName());
                return thread;
            }
        };

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(scheduledThreadPoolSize, namedThreadFactory);
        log.info("Scheduled thread pool created successfully");
        return scheduledExecutorService;
    }

}
