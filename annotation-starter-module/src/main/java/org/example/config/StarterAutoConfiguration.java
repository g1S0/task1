package org.example.config;

import org.example.annotation.LogExecutionTimeAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class StarterAutoConfiguration {
    @Bean
    public LogExecutionTimeAspect myCustomAspect() {
        return new LogExecutionTimeAspect();
    }
}

