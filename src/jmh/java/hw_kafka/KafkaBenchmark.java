package hw_kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.openjdk.jmh.annotations.*;

import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 10)
@Measurement(iterations = 10, time = 5)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
public class KafkaBenchmark {

    private KafkaProducer<String, String> producer;
    private KafkaConsumer<String, String> consumer;
    private String topic;

    @Setup(Level.Trial)
    public void setup() {
        topic = "benchmark-topic-" + UUID.randomUUID();

        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", "localhost:9092");
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(producerProps);

        Properties consumerProps = new Properties();
        consumerProps.put("bootstrap.servers", "localhost:9092");
        consumerProps.put("group.id", "benchmark-group");
        consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("auto.offset.reset", "earliest");
        consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singletonList(topic));
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        producer.close();
        consumer.close();
    }

    @Benchmark
    public void simpleProducerConsumer() {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, "key", "message");
        producer.send(record);

        consumer.poll(1500).forEach(ConsumerRecord::value);
    }

    @Benchmark
    public void multipleProducersOneConsumer() {
        for (int i = 0; i < 3; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, "key" + i, "message" + i);
            producer.send(record);
        }

        consumer.poll(1500).forEach(ConsumerRecord::value);
    }

    @Benchmark
    public void oneProducerMultipleConsumers() {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, "key", "message");
        producer.send(record);

        for (int i = 0; i < 3; i++) {
            consumer.poll(1500).forEach(ConsumerRecord::value);
        }
    }

    @Benchmark
    public void multipleProducersMultipleConsumers() {
        for (int i = 0; i < 3; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, "key" + i, "message" + i);
            producer.send(record);
        }

        for (int i = 0; i < 3; i++) {
            consumer.poll(1500).forEach(ConsumerRecord::value);
        }
    }

    @Benchmark
    public void stressTestProducersConsumers() {
        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, "key" + i, "message" + i);
            producer.send(record);
        }

        for (int i = 0; i < 10; i++) {
            consumer.poll(1500).forEach(ConsumerRecord::value);
        }
    }
}