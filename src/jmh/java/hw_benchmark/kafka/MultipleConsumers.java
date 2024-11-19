package hw_benchmark.kafka;

public class MultipleConsumers extends AbstractKafkaBenchmark {
    @Override
    public int getNumProducers() {
        return 1;
    }

    @Override
    public int getNumConsumers() {
        return 3;
    }

    @Override
    public String getTopic() {
        return "MultipleConsumers";
    }

    @Override
    public String getGroupId() {
        return "MultipleConsumers_group_id";
    }
}

