package pl.sages.javadevpro.projecttwo.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.sages.javadevpro.projecttwo.api.task.TaskDto;
import pl.sages.javadevpro.projecttwo.api.task.TaskDtoMapper;
import pl.sages.javadevpro.projecttwo.api.usertask.UserTaskDto;
import pl.sages.javadevpro.projecttwo.api.usertask.UserTaskDtoMapper;
import pl.sages.javadevpro.projecttwo.domain.UserTaskService;
import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;

@RequiredArgsConstructor
//@RestController
@RequestMapping(path = "/users")
public class UserTaskEndpoint {

    private final UserTaskService userTaskService;
    private final TaskDtoMapper taskDtoMapper;
    private final UserTaskDtoMapper userTaskDtoMapper;

    @PostMapping(
            path = "/{email}/tasks",
            produces = "application/json",
            consumes = "application/json"
    )
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserTaskDto> assignTaskToUser(@PathVariable(name = "email")String userEmail, @RequestBody TaskDto taskDto) {
        Task task = taskDtoMapper.toDomain(taskDto);
        UserTask userTask = userTaskService.createFromTask(task, userEmail);
        return ResponseEntity
                .ok(userTaskDtoMapper.toDto(userTask));
    }
}
