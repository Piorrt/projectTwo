package pl.sages.javadevpro.projecttwo.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.sages.javadevpro.projecttwo.api.usertask.*;
import pl.sages.javadevpro.projecttwo.domain.UserTaskService;
import pl.sages.javadevpro.projecttwo.domain.usertask.TaskStatus;


@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/usertask")
public class UserTaskEndpoint {

    private final UserTaskService userTaskService;
    private final UserTaskDtoMapper dtoMapper;

    @PostMapping(
            path = "/assign",
            produces = "application/json",
            consumes = "application/json"
    )
    @Secured("ROLE_ADMIN")
    public ResponseEntity<MessageResponse> assignTaskToUser(@RequestBody AssignTaskRequest assignTaskRequest) {
        userTaskService.assignTask(assignTaskRequest.getUserEmail(), assignTaskRequest.getTaskId());
        return ResponseEntity.ok(new MessageResponse("OK", "Task assigned to user"));
    }

    @PostMapping("/run")
    @Secured("ROLE_STUDENT")
    public ResponseEntity<String> post(@RequestBody RunSolutionRequest runSolutionRequest) {

        String taskStatus = userTaskService
                .exec(runSolutionRequest.getUserEmail(), runSolutionRequest.getTaskId());
        return ResponseEntity.ok(taskStatus);
    }
}
