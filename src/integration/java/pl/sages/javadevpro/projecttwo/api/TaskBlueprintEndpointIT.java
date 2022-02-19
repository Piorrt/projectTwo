package pl.sages.javadevpro.projecttwo.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.sages.javadevpro.projecttwo.BaseIT;
import pl.sages.javadevpro.projecttwo.api.task.TaskDto;
import pl.sages.javadevpro.projecttwo.domain.task.TaskBlueprintService;
import pl.sages.javadevpro.projecttwo.domain.UserService;
import pl.sages.javadevpro.projecttwo.domain.exception.RecordNotFoundException;
import pl.sages.javadevpro.projecttwo.domain.task.TaskBlueprint;
import pl.sages.javadevpro.projecttwo.domain.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class TaskBlueprintEndpointIT extends BaseIT {

    @Autowired
    UserService userService;
    @Autowired
    TaskBlueprintService taskBlueprintService;

    @Test
    void should_get_information_about_task() {
        //given
        User user = new User(
                "newUser@example.com",
                "User Name",
                "pass",
                List.of("STUDENT"),
                new ArrayList<>()
        );
        TaskBlueprint taskBlueprint = new TaskBlueprint(
                "1",
                "Task Name 1",
                "Task description 1",
                "https://github.com/some-reporitory-1"
        );
        userService.saveUser(user);
        taskBlueprintService.save(taskBlueprint);
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());
        //when
        ResponseEntity<TaskDto> response = callGetTask(1, token);

        //then
        TaskDto body = response.getBody();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(taskBlueprint.getId(), body.getId());
        Assertions.assertEquals(taskBlueprint.getName(), body.getName());
        Assertions.assertEquals(taskBlueprint.getDescription(), body.getDescription());
        Assertions.assertEquals(taskBlueprint.getRepositoryUrl(),body.getRepositoryUrl());
    }

    @Test
    void should_get_information_about_correct_task() {
        //given
        User user = new User(
                "newUser1@example.com",
                "User Name1",
                "pass1",
                List.of("STUDENT"),
                new ArrayList<>()
        );
        TaskBlueprint taskBlueprint2 = new TaskBlueprint(
                "2",
                "Task Name 2",
                "Task description 2",
                "https://github.com/some-reporitory-2"
        );
        TaskBlueprint taskBlueprint3 = new TaskBlueprint(
                "3",
                "Task Name 3",
                "Task description 3",
                "https://github.com/some-reporitory-3"
        );
        TaskBlueprint taskBlueprint4 = new TaskBlueprint(
                "4",
                "Task Name 4",
                "Task description 4",
                "https://github.com/some-reporitory-4"
        );
        userService.saveUser(user);
        taskBlueprintService.save(taskBlueprint2);
        taskBlueprintService.save(taskBlueprint3);
        taskBlueprintService.save(taskBlueprint4);
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());

        //when
        ResponseEntity<TaskDto> response = callGetTask(3, token);

        //then
        TaskDto body = response.getBody();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(taskBlueprint3.getId(), body.getId());
        Assertions.assertEquals(taskBlueprint3.getName(), body.getName());
        Assertions.assertEquals(taskBlueprint3.getDescription(), body.getDescription());
        Assertions.assertEquals(taskBlueprint3.getRepositoryUrl(), body.getRepositoryUrl());
    }

    @Test
     void admin_should_be_able_to_save_new_task() {
        //given
        TaskBlueprint taskBlueprint5 = new TaskBlueprint(
                "5",
                "Task Name 5",
                "Task description 5",
                "https://github.com/some-reporitory-5"
        );
        String adminAccessToken = getTokenForAdmin();
        //when
        ResponseEntity<TaskDto> response = callSaveTask(taskBlueprint5, adminAccessToken);
        //then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        //and
        TaskDto body = response.getBody();
        Assertions.assertEquals(taskBlueprint5.getId(), body.getId());
        Assertions.assertEquals(taskBlueprint5.getName(), body.getName());
        Assertions.assertEquals(taskBlueprint5.getDescription(), body.getDescription());
        Assertions.assertEquals(taskBlueprint5.getRepositoryUrl(), body.getRepositoryUrl());
    }

    @Test
    void student_should_not_be_able_to_save_new_task() {
        //given
        User user = new User(
                "newUser1@example.com",
                "User Name1",
                "pass1",
                List.of("STUDENT"),
                new ArrayList<>()
        );
        TaskBlueprint taskBlueprint5 = new TaskBlueprint(
                "5",
                "Task Name 5",
                "Task description 5",
                "/path/xxx"
        );
        userService.saveUser(user);
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());
        //when
        ResponseEntity<TaskDto> response = callSaveTask(taskBlueprint5, token);
        //then
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    void should_return_conflict_about_duplicated_task(){
        //given
        TaskBlueprint taskBlueprint9 = new TaskBlueprint(
                "9",
                "Task Name 9",
                "Task description 9",
                "/repo/path"
        );
        String adminAccessToken = getTokenForAdmin();
        taskBlueprintService.save(taskBlueprint9);
        //when
        ResponseEntity<TaskDto> response = callSaveTask(taskBlueprint9,adminAccessToken);
        //then
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CONFLICT);
    }

    @Test
    void admin_should_be_able_to_delete_task() {
        //given
        TaskBlueprint taskBlueprint6 = new TaskBlueprint(
                "6",
                "Task Name 6",
                "Task description 6",
                "/repo/path"
        );
        String adminAccessToken = getTokenForAdmin();
        taskBlueprintService.save(taskBlueprint6);
        //when
        callDeleteTask(taskBlueprint6, adminAccessToken);
        //then
        Exception exception = assertThrows(RecordNotFoundException.class, () -> {
            taskBlueprintService.findBy(taskBlueprint6.getId());
        });
        // fixme
        Assertions.assertEquals("Task not found",exception.getMessage());
    }

    @Test
    void student_should_not_be_able_to_delete_task() {
        //given
        User user = new User(
                "newUser@example.com",
                "User Name",
                "pass",
                List.of("STUDENT"),
                new ArrayList<>()
        );
        TaskBlueprint taskBlueprint6 = new TaskBlueprint(
                "6",
                "Task Name 6",
                "Task description 6",
                "/path/path"
        );
        userService.saveUser(user);
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());
        taskBlueprintService.save(taskBlueprint6);

        //when
        ResponseEntity<TaskDto> response = callDeleteTask(taskBlueprint6, token);

        //then
        Assertions.assertEquals(response.getStatusCode(),HttpStatus.FORBIDDEN);
    }

    @Test
    void admin_should_be_able_to_update_task() {
        //given
        TaskBlueprint taskBlueprint7 = new TaskBlueprint(
                "7",
                "Task Name 7",
                "Task description 7",
                "/new/path"
        );
        TaskBlueprint updatedTaskBlueprint = new TaskBlueprint(
                "7",
                "Task Name 7 is updated",
                "Task 7 description is updated ",
                "/no/idea/path"
        );
        String adminAccessToken = getTokenForAdmin();
        taskBlueprintService.save(taskBlueprint7);
        //when
        ResponseEntity<TaskDto> response = callUpdateTask(updatedTaskBlueprint, adminAccessToken);
        //then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        //and
        TaskDto body = response.getBody();
        Assertions.assertEquals(taskBlueprint7.getId(), body.getId());
        Assertions.assertEquals(updatedTaskBlueprint.getName(), body.getName());
        Assertions.assertEquals(updatedTaskBlueprint.getDescription(), body.getDescription());
    }

    @Test
    void should_get_response_code_204_when_task_not_exits() {
        //given
        User user = new User(
                "newUser1@example.com",
                "User Name1",
                "pass1",
                List.of("STUDENT"),
                new ArrayList<>()
        );
        userService.saveUser(user);
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());
        //when
        ResponseEntity<TaskDto> response = callGetTask(1,token);
        //then
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
    }



    private ResponseEntity<TaskDto> callGetTask(int id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.ACCEPT, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, token);
        return restTemplate.exchange(
                localUrl("/tasks/" + id),
                HttpMethod.GET,
                new HttpEntity(headers),
                TaskDto.class
        );
    }

    private ResponseEntity<TaskDto> callSaveTask(TaskBlueprint body, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);
        return restTemplate.exchange(
                localUrl("/tasks"),
                HttpMethod.POST,
                new HttpEntity(body, headers),
                TaskDto.class
        );
    }

    private ResponseEntity<TaskDto> callDeleteTask(TaskBlueprint body, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);
        return restTemplate.exchange(
                localUrl("/tasks"),
                HttpMethod.DELETE,
                new HttpEntity(body, headers),
                TaskDto.class
        );
    }

    private ResponseEntity<TaskDto> callUpdateTask(TaskBlueprint body, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);
        return restTemplate.exchange(
                localUrl("/tasks"),
                HttpMethod.PUT,
                new HttpEntity(body, headers),
                TaskDto.class
        );
    }
}