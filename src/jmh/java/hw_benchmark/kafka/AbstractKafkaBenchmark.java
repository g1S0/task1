package hw_benchmark.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.openjdk.jmh.annotations.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 10)
@Measurement(iterations = 10, time = 5)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
public abstract class AbstractKafkaBenchmark {
    public abstract int getNumProducers();

    public abstract int getNumConsumers();

    public abstract String getTopic();

    public abstract String getGroupId();

    @Benchmark
    public void runKafkaPerformanceTest() {
        String topic = getTopic();
        String groupId = getGroupId();

        List<KafkaProducer<String, String>> producers = createProducers(getNumProducers());
        List<KafkaConsumer<String, String>> consumers = createConsumers(getNumConsumers(), topic, groupId);

        try {
            for (int i = 0; i < producers.size(); i++) {
                KafkaProducer<String, String> producer = producers.get(i);
                String message = "message" + (i + 1);
                producer.send(new ProducerRecord<>(topic, "key" + (i + 1), message));
                producer.flush();
            }

            for (KafkaConsumer<String, String> consumer : consumers) {
                consumer.poll(Duration.ofMillis(1000));
            }
        } finally {
            producers.forEach(KafkaProducer::close);
            consumers.forEach(KafkaConsumer::close);
        }
    }

    private List<KafkaProducer<String, String>> createProducers(int count) {
        List<KafkaProducer<String, String>> producers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Properties producerProps = new Properties();
            producerProps.put("bootstrap.servers", "localhost:9092");
            producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            producers.add(new KafkaProducer<>(producerProps));
        }
        return producers;
    }

    private List<KafkaConsumer<String, String>> createConsumers(int count, String topic, String groupId) {
        List<KafkaConsumer<String, String>> consumers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Properties consumerProps = new Properties();
            consumerProps.put("bootstrap.servers", "localhost:9092");
            consumerProps.put("group.id", groupId);
            consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            consumerProps.put("auto.offset.reset", "earliest");
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
            consumer.subscribe(Collections.singletonList(topic));
            consumers.add(consumer);
        }
        return consumers;
    }
}
