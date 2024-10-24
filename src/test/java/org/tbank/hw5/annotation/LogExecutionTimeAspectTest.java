package org.tbank.hw5.annotation;

import nl.altindag.log.LogCaptor;
import org.example.annotation.LogExecutionTime;
import org.example.annotation.LogExecutionTimeAspect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
public class LogExecutionTimeAspectTest {
    private static final String EXPECTED_LOG_MESSAGE = "Executed method: testMethod in class: TestClassWithLogExecutionTime took";


    @Test
    public void testLogExecutionTimeAspect() throws Throwable {
        try (LogCaptor logCaptor = LogCaptor.forClass(LogExecutionTimeAspect.class)) {
            AspectJProxyFactory factory = new AspectJProxyFactory(new TestClassWithLogExecutionTime());
            LogExecutionTimeAspect logExecutionTimeAspect = new LogExecutionTimeAspect();
            factory.addAspect(logExecutionTimeAspect);
            TestClassWithLogExecutionTime proxy = factory.getProxy();

            proxy.testMethod();

            List<String> logs = logCaptor.getInfoLogs();

            assertTrue(logs.stream().anyMatch(log -> log.contains(EXPECTED_LOG_MESSAGE)));
        }
    }

    public static class TestClassWithLogExecutionTime {
        @LogExecutionTime
        public void testMethod() throws InterruptedException {
            Thread.sleep(100);
        }
    }
}