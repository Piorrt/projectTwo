package pl.sages.javadevpro.projecttwo.api.task;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.sages.javadevpro.projecttwo.api.task.dto.CommandName;
import pl.sages.javadevpro.projecttwo.api.task.dto.TaskControllerCommand;
import pl.sages.javadevpro.projecttwo.api.task.verification.AuthVerifyTask;
import pl.sages.javadevpro.projecttwo.api.usertask.ListOfFilesResponse;
import pl.sages.javadevpro.projecttwo.api.usertask.MessageResponse;
import pl.sages.javadevpro.projecttwo.domain.task.CommitTaskException;
import pl.sages.javadevpro.projecttwo.domain.task.IncorrectTaskStatusException;
import pl.sages.javadevpro.projecttwo.domain.task.TaskCommand;
import pl.sages.javadevpro.projecttwo.domain.task.TaskService;
import pl.sages.javadevpro.projecttwo.domain.task.TaskStatus;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/tasks")
public class TaskController {

    private final TaskService taskService;


    @PostMapping(path = "{taskId}/commands")
    @AuthVerifyTask
    public ResponseEntity<MessageResponse> taskCommand(
            @PathVariable String taskId,
            @RequestBody TaskControllerCommand taskControllerCommand
    ) {
        // TODO ta logika powinna być niżej - done
        //TODO do zrobienia test integracyjny
        String taskStatus = taskService.executeCommand(
                TaskCommand.valueOf(taskControllerCommand.getCommandName().name()),
                taskId
        );
        return ResponseEntity.ok(
                new MessageResponse("Task " + taskId + " executed, status: " + taskStatus)
        );
    }

    @GetMapping(
            produces = "application/json",
            consumes = "application/json",
            path = "{taskId}/files"
    )
    @AuthVerifyTask
    public ResponseEntity<ListOfFilesResponse> getFilesAssignedToUserTask(
            @PathVariable String taskId
    ) {
        List<String> listOfFiles = taskService.getTaskFilesList(taskId);
        return ResponseEntity.ok(new ListOfFilesResponse(
                "OK",
                listOfFiles));
    }

    @GetMapping(
            path = "{taskId}/files/{fileId}"
    )
    @AuthVerifyTask
    public ResponseEntity<Object> getFileAssignedToUserTask(
            @PathVariable String taskId,
            @PathVariable int fileId
    ) {
        return createResponseEntityForFileAssignedToUserTask(taskId, fileId);
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            path = "{taskId}/files/{fileId}"
    )
    @AuthVerifyTask
    public ResponseEntity<MessageResponse> postFileAssignedToUserTask(
            @PathVariable String taskId,
            @PathVariable int fileId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (taskService.getTaskStatus(taskId).equals(TaskStatus.SUBMITTED)) {
            throw new IncorrectTaskStatusException();
        }

        // TODO przenieść do handlera - done
        taskService.writeAndCommitTask(taskId, fileId, file.getBytes());

        return ResponseEntity.ok(new MessageResponse("The File Uploaded Successfully"));
    }

    private ResponseEntity<Object> createResponseEntityForFileAssignedToUserTask(String taskId, int fileId) {
        String filePath = taskService.getTaskFilesList(taskId).get(fileId);
        String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
        byte[] file = taskService.readTaskFile(taskId, filePath);
        HttpHeaders headers = prepareHttpHeadersForFileResponse(fileName);
        return ResponseEntity.ok().headers(headers).contentLength(file.length).contentType(MediaType.parseMediaType("application/txt")).body(file);
    }

    @GetMapping(
            path = "{taskId}/results"
    )
    @AuthVerifyTask
    public ResponseEntity<Object> getUserTaskResult(
            @PathVariable String taskId,
            @Value("${message.testResultFileName}") String fileName
    ) {
        byte[] file = taskService.readTaskResults(taskId);
        HttpHeaders headers = prepareHttpHeadersForFileResponse(fileName);
        return ResponseEntity.ok().headers(headers).contentLength(file.length).contentType(MediaType.parseMediaType("application/txt")).body(file);
    }

    private HttpHeaders prepareHttpHeadersForFileResponse (String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return headers;
    }
}
