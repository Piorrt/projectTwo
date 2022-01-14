package pl.sages.javadevpro.projecttwo.external.executor.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "Kafka_Task_Report_json", groupId = "group_json",
            containerFactory = "taskKafkaListenerFactory")
    public void consumeJson(UserTask userTask) {

        System.out.println("Consumed JSON Task: " + userTask);

    }
}
