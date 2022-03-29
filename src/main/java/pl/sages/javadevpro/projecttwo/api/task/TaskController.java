package pl.sages.javadevpro.projecttwo.api.task;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.sages.javadevpro.projecttwo.api.task.dto.CommandName;
import pl.sages.javadevpro.projecttwo.api.task.dto.TaskControllerCommand;
import pl.sages.javadevpro.projecttwo.api.usertask.ListOfFilesResponse;
import pl.sages.javadevpro.projecttwo.domain.task.TaskService;
import pl.sages.javadevpro.projecttwo.domain.task.TaskStatus;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/tasks")
@PreAuthorize("@assignmentVerifier.userIsOwnerOfTask(#taskId, #authentication)")
public class TaskController {

    private final TaskService taskService;


    @PostMapping(path = "{taskId}/commands")
    public ResponseEntity<Object> taskCommand(
            @RequestBody TaskControllerCommand taskControllerCommand,
            @PathVariable String taskId,
            Authentication authentication) {

        if (taskControllerCommand.getCommandName().equals(CommandName.EXECUTE)) {
            String taskStatus = taskService.execute(taskId);
            return new ResponseEntity<>("Task " + taskId + " executed, status: " + taskStatus, HttpStatus.OK);
        }
        return new ResponseEntity<>("ERROR", HttpStatus.NOT_FOUND);
    }

    @GetMapping(
            produces = "application/json",
            consumes = "application/json",
            path = "{taskId}/files"
    )
    public ResponseEntity<ListOfFilesResponse> getFilesAssignedToUserTask(
            @PathVariable String taskId,
            Authentication authentication) {

        List<String> listOfFiles = taskService.getTaskFilesList(taskId);
        return ResponseEntity.ok(new ListOfFilesResponse(
                "OK",
                listOfFiles));
    }

    @GetMapping(
            path = "{taskId}/files/{fileId}"
    )
    public ResponseEntity<Object> getFileAssignedToUserTask(
            @PathVariable String taskId,
            @PathVariable int fileId,
            Authentication authentication) {

        String filePath = taskService.getTaskFilesList(taskId).get(fileId);
        String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
        byte[] file = taskService.readTaskFile(taskId, filePath);
        HttpHeaders headers = prepareHttpHeadersForFileResponse(fileName);
        return ResponseEntity.ok().headers(headers).contentLength(file.length).contentType(MediaType.parseMediaType("application/txt")).body(file);
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            path = "{taskId}/files/{fileId}"
    )
    public ResponseEntity<Object> postFileAssignedToUserTask(
            @RequestParam("file") MultipartFile file,
            @PathVariable String taskId,
            @PathVariable int fileId,
            Authentication authentication
    ) {
        if (taskService.getTaskStatus(taskId).equals(TaskStatus.SUBMITTED)) {
            return new ResponseEntity<>("The File Upload Failed. The Task was sent to ENV.", HttpStatus.METHOD_NOT_ALLOWED);
        }

        try {
            byte[] bytes = file.getBytes();
            String filePath = taskService.getTaskFilesList(taskId).get(fileId);
            taskService.writeTaskFile(taskId, filePath, bytes);
            taskService.commitTaskChanges(taskId);
        } catch (IOException e) {
            return new ResponseEntity<>("The File Upload Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("The File Uploaded Successfully", HttpStatus.OK);
    }

    @GetMapping(
            path = "{taskId}/results"
    )
    public ResponseEntity<Object> getUserTaskResult(
            @PathVariable String taskId,
            Authentication authentication) {

        String fileName = "task_results.txt";
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
