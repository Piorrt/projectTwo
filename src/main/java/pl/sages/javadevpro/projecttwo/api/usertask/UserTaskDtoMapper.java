package pl.sages.javadevpro.projecttwo.api.usertask;

import org.mapstruct.Mapper;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;

@Mapper
public interface UserTaskDtoMapper {

    UserTaskDto toDto(UserTask userTask);

    UserTask toDomain(UserTaskDto userTaskDto);

}
