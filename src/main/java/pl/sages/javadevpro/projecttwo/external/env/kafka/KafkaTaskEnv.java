package pl.sages.javadevpro.projecttwo.external.env.kafka;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.external.env.task.TaskEnv;


@AllArgsConstructor
 public class KafkaTaskEnv {

    private KafkaTemplate<String, Task> kafkaTemplate;

    private static final String TOPIC = "Kafka_Task_json";

    public String send(Task task) {
        kafkaTemplate.send(TOPIC, task);

        return "Task Send Successfully";
    }
}
