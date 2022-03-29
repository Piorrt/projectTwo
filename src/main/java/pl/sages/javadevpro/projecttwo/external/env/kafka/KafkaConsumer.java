package pl.sages.javadevpro.projecttwo.external.env.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.domain.task.TaskService;
import pl.sages.javadevpro.projecttwo.external.env.task.TaskEnv;
import pl.sages.javadevpro.projecttwo.external.env.task.TaskEnvMapper;

@Log
@AllArgsConstructor
@Service
public class KafkaConsumer {

    private final TaskService taskService;
    private final TaskEnvMapper taskEnvMapper;

    @KafkaListener(topics = "Kafka_Task_Report_json", groupId = "group_json",
            containerFactory = "taskKafkaListenerFactory")
    public void consumeJson(TaskEnv taskEnv) {
        Task task = taskEnvMapper.toDomain(taskEnv);
        log.info("Consumed JSON Task: " + task);
        taskService.updateTaskStatus(task.getId(), task.getStatus());

    }
}
