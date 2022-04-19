package pl.sages.javadevpro.projecttwo.api.task;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sages.javadevpro.projecttwo.api.task.dto.AssigmentRequest;
import pl.sages.javadevpro.projecttwo.api.usertask.MessageResponse;
import pl.sages.javadevpro.projecttwo.domain.assigment.AssigmentService;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/assign")
public class AssignController {

    private final AssigmentService assigmentService;


    @PostMapping(
            produces = "application/json",
            consumes = "application/json"
    )
    public ResponseEntity<MessageResponse> assignTaskToUser(@RequestBody AssigmentRequest assigmentRequest) {
        assigmentService.assignNewTask(assigmentRequest.getUserId(), assigmentRequest.getTaskId());
        return ResponseEntity.ok(new MessageResponse("Task assigned to user"));
    }

}
