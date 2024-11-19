package hw_kafka.kafka;

public class LoadBalancing extends AbstractKafkaBenchmark {
    @Override
    public int getNumProducers() {
        return 3;
    }

    @Override
    public int getNumConsumers() {
        return 1;
    }

    @Override
    public String getTopic() {
        return "LoadBalancing";
    }

    @Override
    public String getGroupId() {
        return "LoadBalancing_group_id";
    }
}

