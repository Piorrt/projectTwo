package pl.sages.javadevpro.projecttwo.external.storage.user;

import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.usertask.TaskStatus;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;
import pl.sages.javadevpro.projecttwo.external.storage.usertask.UserTaskEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class UserEntityMapper {

    public UserEntity toEntity(User user){
        return new UserEntity(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getPassword(),
            user.getRoles(),
            toUserTaskEntity(user.getTasks())
        );
    }

    public User toDomain(UserEntity entity) {
        return new User(
            entity.getId(),
            entity.getEmail(),
            entity.getName(),
            entity.getPassword(),
            entity.getRoles(),
            toUserTask(entity.getTasks(), entity.getId())
        );
    }

    private List<UserTaskEntity> toUserTaskEntity(List<UserTask> tasks) {
        if(tasks != null) {
            return tasks.stream()
                    .map(userTask -> new UserTaskEntity(
                            userTask.getId(),
                            userTask.getName(),
                            userTask.getDescription(),
                            userTask.getUserTaskFolder(),
                            userTask.getTaskStatus().name()
                    )).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private List<UserTask> toUserTask(List<UserTaskEntity> tasks, String userId) {
        if(tasks !=  null) {
            return tasks.stream()
                    .map(userTaskEntity -> new UserTask(
                            userTaskEntity.getId(),
                            userTaskEntity.getName(),
                            userTaskEntity.getDescription(),
                            userTaskEntity.getUserTaskFolder(),
                            TaskStatus.valueOf(userTaskEntity.getTaskStatus()),
                            userId
                    )).collect(Collectors.toList());
        }
        return  Collections.emptyList();
    }
}
