package pl.sages.javadevpro.projecttwo.external.executor;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTaskExecutor;
import pl.sages.javadevpro.projecttwo.external.executor.kafka.KafkaUserTaskExecutor;

@RequiredArgsConstructor
@Log
public class UserTaskExecutorAdapter implements UserTaskExecutor {

   private final KafkaUserTaskExecutor userTaskExecutor;

    @Override
    public String exec(UserTask userTask) {
        return userTaskExecutor.send(userTask);
    }

}
