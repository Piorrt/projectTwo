package pl.sages.javadevpro.projecttwo.external.executor.usertask;

import pl.sages.javadevpro.projecttwo.api.user.UserDto;
import pl.sages.javadevpro.projecttwo.domain.user.User;

public interface UserTaskExecMapper {

    UserDto toDto(User user);

    User toDomain(UserDto userDto);

}
