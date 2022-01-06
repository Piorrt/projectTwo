package pl.sages.javadevpro.projecttwo.api.usertask;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;

@Mapper
public interface UserTaskDtoMapper {

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "name")
    @Mapping(ignore = true, target = "description")
    @Mapping(ignore = true, target = "taskStatus")
    UserTaskDto toDto(UserTask userTask);

    UserTask toDomain(UserTaskDto userTaskDto);

}
