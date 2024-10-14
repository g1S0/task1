package org.tbank.hw5.configuration;

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
public class ExecutorConfig {
    @Value("${fixed.thread.pool.size}")
    private int fixedThreadPoolSize;

    @Value("${scheduled.thread.pool.size}")
    private int scheduledThreadPoolSize;

    @Bean(name = "fixedThreadPool")
    public ExecutorService fixedThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            private final String threadName = "FixedPool-Worker-";

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName(threadName + threadNumber.getAndIncrement());
                return thread;
            }
        };

        return Executors.newFixedThreadPool(fixedThreadPoolSize, namedThreadFactory);
    }


    @Bean(name = "scheduledThreadPool")
    public ScheduledExecutorService scheduledThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            private final String threadName = "ScheduledPool-Worker-";

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName(threadName + threadNumber.getAndIncrement());
                return thread;
            }
        };
        return Executors.newScheduledThreadPool(scheduledThreadPoolSize, namedThreadFactory);
    }

}
