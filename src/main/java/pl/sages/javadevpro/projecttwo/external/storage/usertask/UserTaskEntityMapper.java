package pl.sages.javadevpro.projecttwo.external.storage.usertask;

import org.mapstruct.Mapper;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;

@Mapper
public interface UserTaskEntityMapper {

    UserTask toDomain(UserTaskEntity userTaskEntity);

    UserTaskEntity toEntity(UserTask userTask);

}
