package pl.sages.javadevpro.projecttwo.api;

import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.sages.javadevpro.projecttwo.BaseIT;
import pl.sages.javadevpro.projecttwo.api.task.TaskDtoMapper;
import pl.sages.javadevpro.projecttwo.api.usertask.ListOfFilesResponse;
import pl.sages.javadevpro.projecttwo.api.usertask.MessageResponse;
import pl.sages.javadevpro.projecttwo.api.usertask.UserTaskRequest;
import pl.sages.javadevpro.projecttwo.domain.TaskService;
import pl.sages.javadevpro.projecttwo.domain.UserService;
import pl.sages.javadevpro.projecttwo.domain.UserTaskService;
import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.domain.user.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class UserTaskEndpointIT extends BaseIT {

    @Autowired
    UserTaskService userTaskService;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    TaskDtoMapper taskDtoMapper;

    @Test
    void user_should_not_be_able_to_assign_task() {
        //given
        User user = new User(
                20L,
                "newUser10@example.com",
                "User Name",
                "pass",
                List.of("STUDENT"),
                new ArrayList<>()
        );
        userService.saveUser(user);

        Task task = new Task(
                "1",
                "Task Name 1",
                "Task description 1",
                "https://github.com/Piorrt/projectOne"
        );
        taskService.saveTask(task);
        UserTaskRequest userTaskRequest = new UserTaskRequest(user.getEmail(), task.getId());
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());

        //when
        ResponseEntity<MessageResponse> response = callAssignTask(userTaskRequest, token);

        //then
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void admin_should_be_able_to_assign_task_to_user() {
        User user = new User(
                21L,
                "newUser11@example.com",
                "User Name 11",
                "pass",
                List.of("STUDENT"),
                new ArrayList<>()
        );
        userService.saveUser(user);

        Task task = new Task(
                "1",
                "Task Name 1",
                "Task description 1",
                "https://github.com/Piorrt/projectOne"
        );
        taskService.saveTask(task);
        UserTaskRequest userTaskRequest = new UserTaskRequest(user.getEmail(), task.getId());
        String token = getTokenForAdmin();

        //when
        ResponseEntity<MessageResponse> response = callAssignTask(userTaskRequest, token);
        MessageResponse messageResponse = response.getBody();

        //then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("OK", messageResponse.getStatus());
        Assertions.assertEquals("Task assigned to user", messageResponse.getMessage());


    }

    @Test
    void admin_should_get_conflict_response_when_trying_to_assign_the_same_task_twice(){
        User user = new User(
                22L,
                "newUser13@example.com",
                "User Name 11",
                "pass",
                List.of("STUDENT"),
                new ArrayList<>()
        );
        userService.saveUser(user);

        Task task = new Task(
                "1",
                "Task Name 1",
                "Task description 1",
                "https://github.com/Piorrt/projectOne"
        );
        taskService.saveTask(task);
        UserTaskRequest userTaskRequest = new UserTaskRequest(user.getEmail(), task.getId());
        String token = getTokenForAdmin();

        callAssignTask(userTaskRequest, token);

        //when
        ResponseEntity<MessageResponse> response2 = callAssignTask(userTaskRequest, token);

        //then
        Assertions.assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());
    }

    @Test
    void admin_should_get_204_response_code_when_trying_to_add_task_to_not_existing_user() {
        Task task = new Task(
                "1",
                "Task Name 1",
                "Task description 1",
                "https://github.com/Piorrt/projectOne"
        );
        taskService.saveTask(task);
        UserTaskRequest userTaskRequest = new UserTaskRequest("newUser13@example.com", task.getId());
        String token = getTokenForAdmin();

        //when
        ResponseEntity<MessageResponse> response = callAssignTask(userTaskRequest, token);

        //then
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void student_should_be_able_to_take_list_of_files_assigned_to_user_task() {
        User user = new User(
                23L,
                "newUser11@example.com",
                "User Name 11",
                "pass",
                List.of("STUDENT"),
                new ArrayList<>()
        );
        userService.saveUser(user);

        Task task = new Task(
                "2",
                "Task Name 2",
                "Task description 2",
                "https://github.com/rafal-nowak/task1"
        );
        taskService.saveTask(task);
        UserTaskRequest assignTaskRequest = new UserTaskRequest(user.getEmail(), task.getId());
        String adminToken = getTokenForAdmin();
        String userToken = getAccessTokenForUser(user.getEmail(), user.getPassword());

        //when
        ResponseEntity<MessageResponse> responseAssignTask = callAssignTask(assignTaskRequest, adminToken);
        MessageResponse messageResponse = responseAssignTask.getBody();

        //then
        Assertions.assertEquals(HttpStatus.OK, responseAssignTask.getStatusCode());
        Assertions.assertEquals("OK", messageResponse.getStatus());
        Assertions.assertEquals("Task assigned to user", messageResponse.getMessage());

        //when
        ResponseEntity<ListOfFilesResponse> responseListOfFiles = callGetFilesAssignedToUserTask("newUser11@example.com", "2", userToken);
        ListOfFilesResponse listOfFilesResponse = responseListOfFiles.getBody();

        //then
        ArrayList<String> expectedFileList = new ArrayList<>();
        expectedFileList.add("src/task.py");
        Assertions.assertEquals(HttpStatus.OK, responseListOfFiles.getStatusCode());
        Assertions.assertEquals("OK", listOfFilesResponse.getStatus());
        Assertions.assertEquals(expectedFileList, listOfFilesResponse.getFiles());

    }

    private ResponseEntity<ListOfFilesResponse> callGetFilesAssignedToUserTask (String userId, String taskId, String accessToken) {
        String url = "/usertasks/" + userId + "/" + taskId + "/files";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);

        return restTemplate.exchange(
                localUrl(url),
                HttpMethod.GET,
                new HttpEntity<>("{}",headers),
                ListOfFilesResponse.class
        );
    }

    private ResponseEntity<MessageResponse> callAssignTask(UserTaskRequest body, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);
        return restTemplate.exchange(
                localUrl("/usertasks/assign"),
                HttpMethod.POST,
                new HttpEntity(body, headers),
                MessageResponse.class
        );
    }

    @SneakyThrows
    @AfterAll
    private static void cleanUpFolders(){
        File file = new File("testRepo");
        FileUtils.cleanDirectory(file);
    }

}
