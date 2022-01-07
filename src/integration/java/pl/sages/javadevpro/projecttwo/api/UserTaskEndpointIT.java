package pl.sages.javadevpro.projecttwo.api;

import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import pl.sages.javadevpro.projecttwo.BaseIT;
import pl.sages.javadevpro.projecttwo.api.task.TaskDto;
import pl.sages.javadevpro.projecttwo.api.task.TaskDtoMapper;
import pl.sages.javadevpro.projecttwo.api.usertask.UserTaskDto;
import pl.sages.javadevpro.projecttwo.domain.UserService;
import pl.sages.javadevpro.projecttwo.domain.UserTaskService;
import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.usertask.TaskStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class UserTaskEndpointIT extends BaseIT {

    @Autowired
    UserTaskService userTaskService;

    @Autowired
    UserService userService;

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
        TaskDto taskDto = taskDtoMapper.toDto(task);
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());

        //when
        ResponseEntity<UserTaskDto> response = callAssignTask(taskDto, token, user.getEmail());

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
        TaskDto taskDto = taskDtoMapper.toDto(task);

        String token = getTokenForAdmin();

        //when
        ResponseEntity<UserTaskDto> response = callAssignTask(taskDto, token, user.getEmail());
        UserTaskDto body = response.getBody();

        //then
        Assertions.assertNotNull(body);
        Assertions.assertEquals(task.getId(), body.getId());
        Assertions.assertEquals(task.getDescription(), body.getDescription());
        Assertions.assertEquals(TaskStatus.NOT_STARTED, body.getTaskStatus());
        Assertions.assertEquals(task.getName(), body.getName());
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
        TaskDto taskDto = taskDtoMapper.toDto(task);
        String token = getTokenForAdmin();

        //when
        ResponseEntity<UserTaskDto> response1 = callAssignTask(taskDto, token, user.getEmail());
        ResponseEntity<UserTaskDto> response2 = callAssignTask(taskDto, token, user.getEmail());

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
        TaskDto taskDto = taskDtoMapper.toDto(task);
        String token = getTokenForAdmin();

        //when
        ResponseEntity<UserTaskDto> response = callAssignTask(taskDto, token, "notExist@sample.com");

        //then
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }



    private ResponseEntity<UserTaskDto> callAssignTask(TaskDto body, String accessToken, String userEmail) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);
        return restTemplate.exchange(
                localUrl("/users/" + userEmail + "/tasks"),
                HttpMethod.POST,
                new HttpEntity(body, headers),
                UserTaskDto.class
        );
    }

    @SneakyThrows
    @AfterAll
    private static void cleanUpFolders(){
        File file = new File("userTasks");
        FileUtils.cleanDirectory(file);
    }

}
