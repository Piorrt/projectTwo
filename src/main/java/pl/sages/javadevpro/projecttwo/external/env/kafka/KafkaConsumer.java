package pl.sages.javadevpro.projecttwo.external.env.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;
import pl.sages.javadevpro.projecttwo.external.env.usertask.UserTaskEnv;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "Kafka_Task_Report_json", groupId = "group_json",
            containerFactory = "taskKafkaListenerFactory")
    public void consumeJson(UserTaskEnv userTask) {

        System.out.println("Consumed JSON Task: " + userTask);

    }
}
