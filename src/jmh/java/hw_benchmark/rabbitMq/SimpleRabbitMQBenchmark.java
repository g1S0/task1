package hw_benchmark.rabbitMq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 10)
@Measurement(iterations = 10, time = 5)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
public class SimpleRabbitMQBenchmark {
    private static final String QUEUE_NAME = "simple_queue";
    private static final String EXCHANGE_NAME = "simple_exchange";

    private Connection connection;
    private Channel channel;

    @Setup(Level.Trial)
    public void setup() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");
        connection = factory.newConnection();
        channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
    }

    @TearDown(Level.Trial)
    public void tearDown() throws IOException, TimeoutException {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
        if (connection != null && connection.isOpen()) {
            connection.close();
        }
    }

    @Benchmark
    public void runTest() throws IOException {
        long producerStartTime = System.nanoTime();
        String message = "Test Message";
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        long producerEndTime = System.nanoTime();
        long producerLatency = producerEndTime - producerStartTime;
        System.out.println("SimpleRabbitMQBenchmark Producer latency: " + producerLatency + " ns");

        long consumerStartTime = System.nanoTime();
        channel.basicConsume(QUEUE_NAME, true, (consumerTag, delivery) -> {
        }, consumerTag -> {
        });
        long consumerEndTime = System.nanoTime();
        long consumerLatency = consumerEndTime - consumerStartTime;
        System.out.println("SimpleRabbitMQBenchmark Consumer latency: " + consumerLatency + " ns");
    }
}