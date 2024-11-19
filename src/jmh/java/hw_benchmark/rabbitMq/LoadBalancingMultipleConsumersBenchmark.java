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

@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 10)
@Measurement(iterations = 10, time = 5)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
public class LoadBalancingMultipleConsumersBenchmark {
    private static final String QUEUE_NAME = "load_balancing_queue";
    private static final String EXCHANGE_NAME = "load_balancing_exchange";

    private Connection connection;
    private List<Channel> producers = new ArrayList<>();
    private Channel consumerChannel;

    @Setup(Level.Trial)
    public void setup() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("user");
        factory.setPassword("password");
        connection = factory.newConnection();

        for (int i = 0; i < 3; i++) {
            Channel producer = connection.createChannel();
            producer.exchangeDeclare(EXCHANGE_NAME, "direct", true);
            producer.queueDeclare(QUEUE_NAME, true, false, false, null);
            producer.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
            producers.add(producer);
        }

        consumerChannel = connection.createChannel();
        consumerChannel.queueDeclare(QUEUE_NAME, true, false, false, null);
    }

    @TearDown(Level.Trial)
    public void tearDown() throws IOException, TimeoutException {
        for (Channel producer : producers) {
            if (producer != null && producer.isOpen()) {
                producer.close();
            }
        }
        if (consumerChannel != null && consumerChannel.isOpen()) {
            consumerChannel.close();
        }
        if (connection != null && connection.isOpen()) {
            connection.close();
        }
    }

    @Benchmark
    public void runTest() throws IOException {
        for (int i = 0; i < producers.size(); i++) {
            String message = "Message " + (i + 1);
            producers.get(i).basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        }

        consumerChannel.basicConsume(QUEUE_NAME, true, (consumerTag, delivery) -> {
        }, consumerTag -> {
        });
    }
}