package pl.sages.javadevpro.projecttwo.external.env.usertask;

import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;

public interface UserTaskEnvMapper {

    UserTaskEnv toDto(UserTask userTask);

    UserTask toDomain(UserTaskEnv userTaskExec);

}
