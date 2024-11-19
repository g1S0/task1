package hw_benchmark.kafka;


public class SimpleProducerConsumer extends AbstractKafkaBenchmark {
    @Override
    public int getNumProducers() {
        return 1;
    }

    @Override
    public int getNumConsumers() {
        return 1;
    }

    @Override
    public String getTopic() {
        return "simple";
    }

    @Override
    public String getGroupId() {
        return "simple_group_id";
    }
}