package pl.sages.javadevpro.projecttwo.api.task;

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
import pl.sages.javadevpro.projecttwo.domain.assigment.AssigmentService;
import pl.sages.javadevpro.projecttwo.domain.task.TaskService;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.user.UserService;
import pl.sages.javadevpro.projecttwo.external.workspace.WorkspaceService;
import pl.sages.javadevpro.projecttwo.security.UserPrincipal;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/tasks")
public class TaskController {

    private final AssigmentService assigmentService;
    private final WorkspaceService workspaceService;
    private final UserService userService;
    private final TaskService taskService;

    @PostMapping(
            path = "/assign",
            produces = "application/json",
            consumes = "application/json"
    )
    @Secured("ROLE_ADMIN")
    public ResponseEntity<MessageResponse> assignTaskToUser(@RequestBody AssigmentRequest assigmentRequest) {
        assigmentService.assignNewTask(assigmentRequest.getUserId(), assigmentRequest.getTaskId());
        return ResponseEntity.ok(new MessageResponse("OK", "Task assigned to user"));
    }

//    @PostMapping("/run")
//    @Secured("ROLE_STUDENT")
//    public ResponseEntity<String> post(@RequestBody RunSolutionRequest runSolutionRequest) {
//        String taskStatus = userTaskService
//                .exec(runSolutionRequest.getUserEmail(), runSolutionRequest.getTaskId());
//
//        return ResponseEntity.ok(taskStatus);
//    }

    @GetMapping(
            produces = "application/json",
            consumes = "application/json",
            path = "{taskId}/files"
    )
    public ResponseEntity<Object>  getFilesAssignedToUserTask(
            @PathVariable String taskId,
            Authentication authentication) {

        System.out.println(authentication.getPrincipal().getClass());
        User user = userService.findByEmail(((UserPrincipal) authentication.getPrincipal()).getUsername());
        List<String> listOfFiles = null;
        if (assigmentService.isTaskAssignedToUser(user.getId(), taskId)){
            listOfFiles = taskService.getTaskFilesList(taskId);
            return ResponseEntity.ok(new ListOfFilesResponse(
                    "OK",
                    listOfFiles));
        }
        return new ResponseEntity<>("ERROR", HttpStatus.NOT_FOUND);
    }

    @GetMapping(
            path = "{taskId}/files/{fileId}"
    )
    public ResponseEntity<Object>  getFileAssignedToUserTask(
            @PathVariable String taskId,
            @PathVariable String fileId,
            Authentication authentication) {

        System.out.println(authentication.getPrincipal().getClass());
        User user = userService.findByEmail(((UserPrincipal) authentication.getPrincipal()).getUsername());

        if (assigmentService.isTaskAssignedToUser(user.getId(), taskId)){
            String filePath = taskService.getTaskFilesList(taskId).get(parseInt(fileId));
            String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
            byte[] file = taskService.readTaskFile(taskId, filePath);
            InputStreamResource resource;
            resource = new InputStreamResource(new ByteArrayInputStream(file));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity.ok().headers(headers).contentLength(file.length).contentType(MediaType.parseMediaType("application/txt")).body(resource);

        }

        return new ResponseEntity<>("ERROR", HttpStatus.NOT_FOUND);

    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            path = "{taskId}/files/{fileId}"
    )
    @Secured("ROLE_STUDENT")
    public ResponseEntity<Object>  postFileAssignedToUserTask(
            @RequestParam("file") MultipartFile file,
            @PathVariable String taskId,
            @PathVariable String fileId,
            Authentication authentication
            ) {

        System.out.println(authentication.getPrincipal().getClass());
        User user = userService.findByEmail(((UserPrincipal) authentication.getPrincipal()).getUsername());

        if (assigmentService.isTaskAssignedToUser(user.getId(), taskId)){
            try {
                byte[] bytes = file.getBytes();
                String filePath = taskService.getTaskFilesList(taskId).get(parseInt(fileId));

                taskService.writeTaskFile(taskId, filePath, bytes);

                taskService.commitTaskChanges(taskId);

            } catch (IOException e) {
                return new ResponseEntity<>("The File Upload Failed", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>("The File Uploaded Successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("ERROR", HttpStatus.NOT_FOUND);

    }

//    @GetMapping(
//            path = "/results",
//            produces = "application/json",
//            consumes = "application/json"
//    )
//    @Secured("ROLE_STUDENT")
//    public ResponseEntity<String> getUserTaskResult(@RequestBody UserTaskRequest userTaskRequest){
//        String resultSummary = userTaskService.getUserTaskStatusSummary(userTaskRequest.getUserId(), userTaskRequest.getTaskId());
//        return ResponseEntity.ok(resultSummary);
//    }

}