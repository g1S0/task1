
# Performance Test Results

## Kafka Benchmarks
| Benchmark                                               | Mode  | Cnt  | Score    | Error    | Units |
|---------------------------------------------------------|-------|-------|----------|----------|-------|
| h.kafka.LoadBalancing.runKafkaPerformanceTest           | thrpt | 10    | 0.325    | ± 0.001  | ops/s |
| h.kafka.LoadBalancingMultipleConsumers.runKafkaTest     | thrpt | 10    | 0.324    | ± 0.003  | ops/s |
| h.kafka.MultipleConsumers.runKafkaPerformanceTest       | thrpt | 10    | 0.326    | ± 0.001  | ops/s |
| h.kafka.SimpleProducerConsumer.runKafkaPerformanceTest  | thrpt | 10    | 0.326    | ± 0.001  | ops/s |
| h.kafka.StressTest.runKafkaPerformanceTest              | thrpt | 10    | 0.099    | ± 0.001  | ops/s |

## RabbitMQ Benchmarks
| Benchmark                                               | Mode  | Cnt  | Score    | Error    | Units |
|---------------------------------------------------------|-------|-------|----------|----------|-------|
| h.rabbitMq.LoadBalancingMultipleConsumers.runTest       | thrpt | 10    | 361.018  | ± 80.528 | ops/s |
| h.rabbitMq.LoadBalancingRabbitMQ.runTest                | thrpt | 10    | 277.722  | ± 199.668| ops/s |
| h.rabbitMq.MultipleConsumersRabbitMQ.runTest            | thrpt | 10    | 89.453   | ± 56.689 | ops/s |
| h.rabbitMq.SimpleRabbitMQ.runTest                       | thrpt | 10    | 350.048  | ± 100.733| ops/s |
| h.rabbitMq.StressTestRabbitMQ.runTest                   | thrpt | 10    | 29.864   | ± 11.990 | ops/s |


# Kafka и RabbitMQ: Анализ производительности

## Kafka

- Kafka показывает стабильную производительность при использовании большого числа продюсеров и консюмеров, особенно для приложений, требующих высокой пропускной способности и низкой задержки.
- Использование нескольких консюмеров (`MultipleConsumers`) или базовая схема "продюсер-консюмер" дают стабильный результат ~0.326 ops/s.
- Задержка увеличивается при нагрузке, что видно из теста `StressTest` (только 0.099 ops/s).

## RabbitMQ

- Максимальная производительность достигается в тесте `LoadBalancingMultipleConsumers` (361 ops/s).
- RabbitMQ теряет производительность при обработке большого объема сообщений, например, в тесте `StressTest` производительность составила всего 29.86 ops/s.


# Сценарии использования RabbitMQ и Kafka

| **Сценарий**                               | **RabbitMQ**                                 | **Kafka**                                   |
|--------------------------------------------|---------------------------------------------|--------------------------------------------|
| **Высокая пропускная способность**         | Не подходит из-за узких мест.               | Отлично справляется благодаря партициям.   |
| **Маршрутизация сообщений**                | Идеально из-за гибких exchange-ов.          | Менее гибкий, лучше использовать топики.   |
| **Долгосрочное хранение данных**           | Не рекомендуется.                          | Идеально подходит для этого.               |
| **Системы с низкой задержкой**             | Превосходно.                               | Не оптимально для небольших нагрузок.      |
| **Масштабируемость в распределённых системах** | Сложнее реализовать.                        | Превосходно за счёт партиций и реплик.     |
| **Обработка транзакций**                   | Лучшая поддержка транзакций.                | Подходит только с ограничениями.           |



