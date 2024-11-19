package hw_kafka.kafka;

public class LoadBalancingMultipleConsumers extends AbstractKafkaBenchmark {
    @Override
    public int getNumProducers() {
        return 3;
    }

    @Override
    public int getNumConsumers() {
        return 3;
    }

    @Override
    public String getTopic() {
        return "LoadBalancingMultipleConsumers";
    }

    @Override
    public String getGroupId() {
        return "LoadBalancingMultipleConsumers_group_id";
    }
}

