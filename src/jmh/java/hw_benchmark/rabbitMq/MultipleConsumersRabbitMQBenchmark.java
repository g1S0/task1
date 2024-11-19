package hw_benchmark.rabbitMq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 10)
@Measurement(iterations = 10, time = 5)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
public class MultipleConsumersRabbitMQBenchmark {
    private static final String QUEUE_NAME = "multiple_consumers_queue";
    private static final String EXCHANGE_NAME = "multiple_consumers_exchange";

    private Connection connection;
    private Channel producerChannel;
    private List<Channel> consumers = new ArrayList<>();

    @Setup(Level.Trial)
    public void setup() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");
        connection = factory.newConnection();

        producerChannel = connection.createChannel();
        producerChannel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
        producerChannel.queueDeclare(QUEUE_NAME, true, false, false, null);
        producerChannel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

        for (int i = 0; i < 3; i++) {
            Channel consumer = connection.createChannel();
            consumer.queueDeclare(QUEUE_NAME, true, false, false, null);
            consumers.add(consumer);
        }
    }

    @TearDown(Level.Trial)
    public void tearDown() throws IOException, TimeoutException {
        if (producerChannel != null && producerChannel.isOpen()) {
            producerChannel.close();
        }
        for (Channel consumer : consumers) {
            if (consumer != null && consumer.isOpen()) {
                consumer.close();
            }
        }
        if (connection != null && connection.isOpen()) {
            connection.close();
        }
    }

    @Benchmark
    public void runTest() throws IOException {
        String message = "Hello, RabbitMQ!";
        producerChannel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());

        for (Channel consumer : consumers) {
            consumer.basicConsume(QUEUE_NAME, true, (consumerTag, delivery) -> {
            }, consumerTag -> {
            });
        }
    }
}

