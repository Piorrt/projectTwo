package pl.sages.javadevpro.projecttwo.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.sages.javadevpro.projecttwo.api.usertask.AssignTaskRequest;
import pl.sages.javadevpro.projecttwo.api.usertask.MessageResponse;
import pl.sages.javadevpro.projecttwo.domain.UserTaskService;



@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/usertask/assign")
public class UserTaskEndpoint {

    private final UserTaskService userTaskService;

    @PostMapping(
            produces = "application/json",
            consumes = "application/json"
    )
    @Secured("ROLE_ADMIN")
    public ResponseEntity<MessageResponse> assignTaskToUser(@RequestBody AssignTaskRequest assignTaskRequest) {
        userTaskService.assignTask(assignTaskRequest.getUserEmail(), assignTaskRequest.getTaskId());
        return ResponseEntity.ok(new MessageResponse("OK", "Task assigned to user"));
    }
}
