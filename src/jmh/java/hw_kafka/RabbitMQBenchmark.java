package hw_kafka;

import org.openjdk.jmh.annotations.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 10)
@Measurement(iterations = 10, time = 5)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
public class RabbitMQBenchmark {

    private RabbitTemplate rabbitTemplate;
    private String queueName;
    private CountDownLatch latch;

    @Setup(Level.Trial)
    public void setup() {
        queueName = "benchmark-queue";

        rabbitTemplate = new RabbitTemplate(connectionFactory());

        latch = new CountDownLatch(1);
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        rabbitTemplate = null;
    }

    @Benchmark
    public void simpleProducerConsumer() {
        producerSend("message");
        consumerReceive();
    }

    @Benchmark
    public void multipleProducersOneConsumer() {
        for (int i = 0; i < 3; i++) {
            producerSend("message" + i);
        }
        consumerReceive();
    }

    @Benchmark
    public void oneProducerMultipleConsumers() {
        producerSend("message");
        for (int i = 0; i < 3; i++) {
            consumerReceive();
        }
    }

    @Benchmark
    public void multipleProducersMultipleConsumers() {
        for (int i = 0; i < 3; i++) {
            producerSend("message" + i);
        }
        for (int i = 0; i < 3; i++) {
            consumerReceive();
        }
    }

    @Benchmark
    public void stressTestProducersConsumers() {
        for (int i = 0; i < 10; i++) {
            producerSend("message" + i);
        }
        for (int i = 0; i < 10; i++) {
            consumerReceive();
        }
    }

    private void producerSend(String message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }

    private void consumerReceive() {
        try {
            latch.await(1500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @RabbitListener(queues = "benchmark-queue")
    public void listener(String message) {
        latch.countDown();
    }

    private ConnectionFactory connectionFactory() {
        org.springframework.amqp.rabbit.connection.CachingConnectionFactory factory = new org.springframework.amqp.rabbit.connection.CachingConnectionFactory("localhost");
        factory.setUsername("user");
        factory.setPassword("password");
        factory.setVirtualHost("/");
        return factory;
    }
}