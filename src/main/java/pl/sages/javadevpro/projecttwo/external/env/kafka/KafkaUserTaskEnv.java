package pl.sages.javadevpro.projecttwo.external.env.kafka;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import pl.sages.javadevpro.projecttwo.external.env.usertask.UserTaskEnv;


@AllArgsConstructor
 public class KafkaUserTaskEnv {

    private KafkaTemplate<String, UserTaskEnv> kafkaTemplate;

    private static final String TOPIC = "Kafka_Task_json";

    public String send(UserTaskEnv userTaskEnv) {
        kafkaTemplate.send(TOPIC, userTaskEnv);

        return "Task Sent Successfully 111111111";
    }
}
