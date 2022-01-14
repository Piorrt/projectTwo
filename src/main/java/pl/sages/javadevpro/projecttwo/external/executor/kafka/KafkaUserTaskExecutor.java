package pl.sages.javadevpro.projecttwo.external.executor.kafka;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;


@AllArgsConstructor
 public class KafkaUserTaskExecutor {

    private KafkaTemplate<String, UserTask> kafkaTemplate;

    private static final String TOPIC = "Kafka_Task_json";

    public String send(UserTask userTask) {
        kafkaTemplate.send(TOPIC, userTask);

        return "Task Sent Successfully 111111111";
    }
}
