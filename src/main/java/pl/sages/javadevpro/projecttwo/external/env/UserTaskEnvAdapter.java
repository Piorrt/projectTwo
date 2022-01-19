package pl.sages.javadevpro.projecttwo.external.env;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTaskExecutor;
import pl.sages.javadevpro.projecttwo.external.env.kafka.KafkaUserTaskEnv;
import pl.sages.javadevpro.projecttwo.external.env.usertask.UserTaskEnvMapper;

@RequiredArgsConstructor
@Log
public class UserTaskEnvAdapter implements UserTaskExecutor {

   private final KafkaUserTaskEnv userTaskExecutor;
   private final UserTaskEnvMapper execMapper;

    @Override
    public String exec(UserTask userTask) {
        return userTaskExecutor.send(execMapper.toDto(userTask));
    }

}
