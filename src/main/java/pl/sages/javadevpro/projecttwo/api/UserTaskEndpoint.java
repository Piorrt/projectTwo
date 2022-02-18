package pl.sages.javadevpro.projecttwo.api;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.sages.javadevpro.projecttwo.api.usertask.*;
import pl.sages.javadevpro.projecttwo.domain.UserService;
import pl.sages.javadevpro.projecttwo.domain.UserTaskService;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTaskRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/usertasks")
public class UserTaskEndpoint {

    private final UserTaskService userTaskService;
    private final UserService userService;
    private final UserTaskDtoMapper userTaskDtoMapper;

    @PostMapping(
            produces = "application/json",
            consumes = "application/json"
    )
    @Secured("ROLE_ADMIN")
    public ResponseEntity<MessageResponse> assignTaskToUser(UserTaskRequest userTaskRequest) {
        userTaskService.assignTask(userTaskRequest.getUserEmail(), userTaskRequest.getTaskId());
        return ResponseEntity.ok(new MessageResponse("OK", "Task assigned to user"));
    }


    @PostMapping("/{taskId}/run")
    @Secured("ROLE_STUDENT")
    public ResponseEntity<String> post(Authentication authentication, @PathVariable String taskId) {
        User loggedUser = getLoggedUser(authentication);
        String taskStatus = userTaskService.exec(loggedUser, taskId);
        return ResponseEntity.ok(taskStatus);
    }

    @GetMapping(
            produces = "application/json",
            consumes = "application/json",
            path = "/{taskId}/files"
    )
    @Secured("ROLE_STUDENT")
    public ResponseEntity<ListOfFilesResponse>  getFilesAssignedToUserTask(
            Authentication authentication,
            @PathVariable String taskId) {
        User loggedUser = getLoggedUser(authentication);
        List<String> listOfFiles = userTaskService.readListOfAvailableFilesForUserTask(loggedUser, taskId);

        return ResponseEntity.ok(new ListOfFilesResponse(
                "OK",
                listOfFiles));
    }

    @GetMapping(
            path = "/{taskId}/files/{fileId}"
    )
    @Secured("ROLE_STUDENT")
    public ResponseEntity<Object>  getFileAssignedToUserTask(
            Authentication authentication,
            @PathVariable String taskId,
            @PathVariable String fileId) {

        User loggedUser = getLoggedUser(authentication);
        InputStreamResource resource;
        try {
            File file = userTaskService.takeFileFromUserTask(loggedUser, taskId, fileId);

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

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            path = "/{taskId}/files/{fileId}"
    )
    @Secured("ROLE_STUDENT")
    public ResponseEntity<Object>  postFileAssignedToUserTask(
            Authentication authentication,
            @RequestParam("file") MultipartFile file,
            @PathVariable String taskId,
            @PathVariable String fileId
            ) {

        User loggedUser = getLoggedUser(authentication);
        try {
            byte[] bytes = file.getBytes();

            userTaskService.uploadFileForUserTask(loggedUser, taskId, fileId, bytes);

            userTaskService.commitTask(loggedUser, taskId);

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
    public ResponseEntity<String> getUserTaskResult(Authentication authentication, @PathVariable String taskId){
        User loggedUser = getLoggedUser(authentication);
        String resultSummary = userTaskService.getUserTaskStatusSummary(loggedUser, taskId);
        return ResponseEntity.ok(resultSummary);
    }

    @GetMapping(
            produces = "application/json",
            consumes = "application/json"
    )
    @Secured("ROLE_STUDENT")
    public List<UserTaskDto> getUsersTasks(Authentication authentication) {
        User loggedUser = getLoggedUser(authentication);
        return loggedUser.getTasks().stream()
                .map(userTaskDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    private User getLoggedUser(Authentication authentication) {
        String loggedUserEmail = (String) authentication.getPrincipal();
        return userService.getUserByEmail(loggedUserEmail);
    }

}
