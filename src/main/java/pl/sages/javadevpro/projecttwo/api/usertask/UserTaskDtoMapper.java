package pl.sages.javadevpro.projecttwo.api.usertask;

import pl.sages.javadevpro.projecttwo.api.user.UserDto;
import pl.sages.javadevpro.projecttwo.domain.user.User;

@Mapper
public interface UserTaskDtoMapper {

    UserTaskDto toDto(UserTask userTask);

    UserTask toDomain(UserTaskDto userTaskDto);

}
