package pl.sages.javadevpro.projecttwo.external.env.usertask;

import org.mapstruct.Mapper;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;

@Mapper
public interface UserTaskEnvMapper {

    UserTaskEnv toDto(UserTask userTask);

    UserTask toDomain(UserTaskEnv userTaskExec);

}
