package pl.sages.javadevpro.projecttwo.external.env.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.sages.javadevpro.projecttwo.domain.UserService;
import pl.sages.javadevpro.projecttwo.domain.UserTaskService;
import pl.sages.javadevpro.projecttwo.domain.exception.RecordNotFoundException;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;
import pl.sages.javadevpro.projecttwo.external.env.usertask.UserTaskEnv;
import pl.sages.javadevpro.projecttwo.external.env.usertask.UserTaskEnvMapper;

import java.util.List;

@Log
@AllArgsConstructor
@Service
public class KafkaConsumer {

    private final UserService userService;
    private final UserTaskService userTaskService;
    private final UserTaskEnvMapper userTaskEnvMapper;

    @KafkaListener(topics = "Kafka_Task_Report_json", groupId = "group_json",
            containerFactory = "taskKafkaListenerFactory")
    public void consumeJson(UserTaskEnv userTaskEnv) {
        log.info("Consumed JSON Task: " + userTaskEnv);
        UserTask userTaskFromEnv = userTaskEnvMapper.toDomain(userTaskEnv);
        User user = userService.getUserByEmail(userTaskEnv.getUserEmail());
        List<UserTask> tasks = user.getTasks();
        UserTask taskToStatusUpdate = tasks.stream()
                .filter(task -> task.getId().equals(userTaskFromEnv.getId()))
                .findFirst()
                .orElseThrow(() -> new RecordNotFoundException("Task is not assigned to user"));

        taskToStatusUpdate.setTaskStatus(userTaskFromEnv.getTaskStatus());
        userTaskService.updateUserTaskInDB(taskToStatusUpdate, user);
    }
}
