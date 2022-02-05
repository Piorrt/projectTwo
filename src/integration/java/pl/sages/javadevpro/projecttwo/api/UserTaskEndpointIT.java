package pl.sages.javadevpro.projecttwo.api;

import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import pl.sages.javadevpro.projecttwo.BaseIT;
import pl.sages.javadevpro.projecttwo.api.task.TaskDtoMapper;
import pl.sages.javadevpro.projecttwo.api.usertask.AssignTaskRequest;
import pl.sages.javadevpro.projecttwo.api.usertask.MessageResponse;
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
        AssignTaskRequest assignTaskRequest = new AssignTaskRequest(user.getEmail(), task.getId());
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());

        //when
        ResponseEntity<MessageResponse> response = callAssignTask(assignTaskRequest, token);

        //then
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void admin_should_be_able_to_assign_task_to_user() {
        User user = new User(
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
        AssignTaskRequest assignTaskRequest = new AssignTaskRequest(user.getEmail(), task.getId());
        String token = getTokenForAdmin();

        //when
        ResponseEntity<MessageResponse> response = callAssignTask(assignTaskRequest, token);
        MessageResponse messageResponse = response.getBody();

        //then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("OK", messageResponse.getStatus());
        Assertions.assertEquals("Task assigned to user", messageResponse.getMessage());


    }

    @Test
    void admin_should_get_conflict_response_when_trying_to_assign_the_same_task_twice(){
        User user = new User(
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
        AssignTaskRequest assignTaskRequest = new AssignTaskRequest(user.getEmail(), task.getId());
        String token = getTokenForAdmin();

        //when
        ResponseEntity<MessageResponse> response1 = callAssignTask(assignTaskRequest, token);
        ResponseEntity<MessageResponse> response2 = callAssignTask(assignTaskRequest, token);

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
        AssignTaskRequest assignTaskRequest = new AssignTaskRequest("notExist@sample.com", task.getId());
        String token = getTokenForAdmin();

        //when
        ResponseEntity<MessageResponse> response = callAssignTask(assignTaskRequest, token);

        //then
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }



    private ResponseEntity<MessageResponse> callAssignTask(AssignTaskRequest body, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);
        return restTemplate.exchange(
                localUrl("/usertask/assign"),
                HttpMethod.POST,
                new HttpEntity(body, headers),
                MessageResponse.class
        );
    }

//fixme cleanDirectory should not delete files from business app
    @SneakyThrows
    @AfterAll
    private static void cleanUpFolders(){
        File file = new File("userTasks");
        FileUtils.cleanDirectory(file);
    }

}
