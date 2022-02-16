package pl.sages.javadevpro.projecttwo.api;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.sages.javadevpro.projecttwo.api.usertask.*;
import pl.sages.javadevpro.projecttwo.domain.UserTaskService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users/{userId}/tasks")
public class UserTaskEndpoint {

    private final UserTaskService userTaskService;


    @PostMapping(
            produces = "application/json",
            consumes = "application/json"
    )
    @Secured("ROLE_ADMIN")
    public ResponseEntity<MessageResponse> assignTaskToUser(@PathVariable String userId, @RequestParam String taskId) {
        userTaskService.assignTask(userId, taskId);
        return ResponseEntity.ok(new MessageResponse("OK", "Task assigned to user"));
    }


    //TODO endpoint ot be discussed (AS)
    @PostMapping("/{taskId}/run")
    @Secured("ROLE_STUDENT")
    public ResponseEntity<String> post(@PathVariable String userId, @PathVariable String taskId) {
        String taskStatus = userTaskService.exec(userId, taskId);
        return ResponseEntity.ok(taskStatus);
    }

    @GetMapping(
            produces = "application/json",
            consumes = "application/json",
            path = "/{taskId}/files"
    )
    @Secured("ROLE_STUDENT")
    public ResponseEntity<ListOfFilesResponse>  getFilesAssignedToUserTask(
            @PathVariable String userId,
            @PathVariable String taskId) {

        List<String> listOfFiles = userTaskService.readListOfAvailableFilesForUserTask(userId, taskId);

        return ResponseEntity.ok(new ListOfFilesResponse(
                "OK",
                listOfFiles));
    }

    @GetMapping(
            path = "/{taskId}/files/{fileId}"
    )
    @Secured("ROLE_STUDENT")
    public ResponseEntity<Object>  getFileAssignedToUserTask(
            @PathVariable String userId,
            @PathVariable String taskId,
            @PathVariable String fileId) {

        InputStreamResource resource;
        try {
            File file = userTaskService.takeFileFromUserTask(userId, taskId, fileId);

            resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/txt")).body(resource);
        } catch (FileNotFoundException e) {
            return new ResponseEntity<>("error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // TODO - discuss PUT method instead of POST
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            path = "/{taskId}/files/{fileId}"
    )
    @Secured("ROLE_STUDENT")
    public ResponseEntity<Object>  postFileAssignedToUserTask(
            @RequestParam("file") MultipartFile file,
            @PathVariable String userId,
            @PathVariable String taskId,
            @PathVariable String fileId
            ) {

        try {
            byte[] bytes = file.getBytes();

            userTaskService.uploadFileForUserTask(userId, taskId, fileId, bytes);

            userTaskService.commitTask(userId, taskId);

        } catch (IOException e) {
            return new ResponseEntity<>("The File Upload Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("The File Uploaded Successfully", HttpStatus.OK);
    }

    @GetMapping(
            path = "/{taskId}/results",
            produces = "application/json",
            consumes = "application/json"
    )
    @Secured("ROLE_STUDENT")
    public ResponseEntity<String> getUserTaskResult(@PathVariable String userId, @PathVariable String taskId){
        String resultSummary = userTaskService.getUserTaskStatusSummary(userId, taskId);
        return ResponseEntity.ok(resultSummary);
    }

}
