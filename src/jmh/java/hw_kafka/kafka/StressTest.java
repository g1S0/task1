package hw_kafka.kafka;

public class StressTest extends AbstractKafkaBenchmark {
    @Override
    public int getNumProducers() {
        return 10;
    }

    @Override
    public int getNumConsumers() {
        return 10;
    }

    @Override
    public String getTopic() {
        return "StressTest";
    }

    @Override
    public String getGroupId() {
        return "StressTest_group_id";
    }
}