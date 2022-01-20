package pl.sages.javadevpro.projecttwo.external.storage.user;

import org.mapstruct.Mapper;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.usertask.TaskStatus;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;
import pl.sages.javadevpro.projecttwo.external.storage.usertask.UserTaskEntity;

import java.util.stream.Collectors;


public class UserEntityMapper {

    public UserEntity toEntity(User user){
        return new UserEntity(
            user.getEmail(),
            user.getName(),
            user.getPassword(),
            user.getRoles(),
            user.getTasks().stream()
                .map(userTask -> new UserTaskEntity(
                    userTask.getId(),
                    userTask.getName(),
                    userTask.getDescription(),
                    userTask.getUserTaskFolder(),
                    userTask.getTaskStatus().name()
                )).collect(Collectors.toList())
        );
    }

    public User toDomain(UserEntity entity) {
        return new User(
            entity.getEmail(),
            entity.getName(),
            entity.getPassword(),
            entity.getRoles(),
            entity.getTasks().stream()
                .map(userTask -> new UserTask(
                    userTask.getId(),
                    userTask.getName(),
                    userTask.getDescription(),
                    userTask.getUserTaskFolder(),
                    TaskStatus.valueOf(userTask.getTaskStatus()),
                    entity.getEmail()
                )).collect(Collectors.toList())
        );
    }
}
