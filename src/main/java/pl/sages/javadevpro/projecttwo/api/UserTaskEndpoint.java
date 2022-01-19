package pl.sages.javadevpro.projecttwo.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.sages.javadevpro.projecttwo.api.usertask.AssignTaskRequest;
import pl.sages.javadevpro.projecttwo.api.usertask.MessageResponse;
import pl.sages.javadevpro.projecttwo.api.usertask.UserTaskDto;
import pl.sages.javadevpro.projecttwo.api.usertask.UserTaskDtoMapper;
import pl.sages.javadevpro.projecttwo.domain.UserTaskService;
import pl.sages.javadevpro.projecttwo.domain.usertask.TaskStatus;


@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/usertask/assign")
public class UserTaskEndpoint {

    private final UserTaskService userTaskService;
    private final UserTaskDtoMapper dtoMapper;

    @PostMapping(
            produces = "application/json",
            consumes = "application/json"
    )
    @Secured("ROLE_ADMIN")
    public ResponseEntity<MessageResponse> assignTaskToUser(@RequestBody AssignTaskRequest assignTaskRequest) {
        userTaskService.assignTask(assignTaskRequest.getUserEmail(), assignTaskRequest.getTaskId());
        return ResponseEntity.ok(new MessageResponse("OK", "Task assigned to user"));
    }

    @GetMapping("/sendtask/{taskId}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<String> post(@PathVariable("taskId") final String taskId) {


        UserTaskDto userTaskDto = new UserTaskDto(
                "12",
                "Task2",
                "super fajny task",
                TaskStatus.STARTED
        );

        String taskStatus = userTaskService
                .exec(dtoMapper.toDomain(userTaskDto));
        return ResponseEntity.ok(taskStatus);
    }
    

}
