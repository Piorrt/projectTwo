package pl.sages.javadevpro.projecttwo.external.storage.usertask;

import org.mapstruct.Mapper;
import pl.sages.javadevpro.projecttwo.domain.task.Task;

@Mapper
public interface UserTaskEntityMapper {

    Task toDomain(UserTaskEntity entity);

    UserTaskEntity toEntity(Task domain);
}