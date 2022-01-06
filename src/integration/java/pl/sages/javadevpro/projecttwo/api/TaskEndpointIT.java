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
import pl.sages.javadevpro.projecttwo.domain.TaskService;
import pl.sages.javadevpro.projecttwo.domain.UserService;
import pl.sages.javadevpro.projecttwo.domain.exception.RecordNotFoundException;
import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.domain.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class TaskEndpointIT extends BaseIT {

    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;

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
        Task task = new Task(
                "1",
                "Task Name 1",
                "Task description 1",
                "https://github.com/some-reporitory-1"
        );
        userService.saveUser(user);
        taskService.saveTask(task);
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());

        //when
        ResponseEntity<TaskDto> response = callGetTask(1, token);

        //then
        TaskDto body = response.getBody();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(task.getId(), body.getId());
        Assertions.assertEquals(task.getName(), body.getName());
        Assertions.assertEquals(task.getDescription(), body.getDescription());
        Assertions.assertEquals(task.getRepositoryPath(),body.getRepositoryPath());
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
        Task task2 = new Task(
                "2",
                "Task Name 2",
                "Task description 2",
                "https://github.com/some-reporitory-2"
        );
        Task task3 = new Task(
                "3",
                "Task Name 3",
                "Task description 3",
                "https://github.com/some-reporitory-3"
        );
        Task task4 = new Task(
                "4",
                "Task Name 4",
                "Task description 4",
                "https://github.com/some-reporitory-4"
        );
        userService.saveUser(user);
        taskService.saveTask(task2);
        taskService.saveTask(task3);
        taskService.saveTask(task4);
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());

        //when
        ResponseEntity<TaskDto> response = callGetTask(3, token);

        //then
        TaskDto body = response.getBody();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(task3.getId(), body.getId());
        Assertions.assertEquals(task3.getName(), body.getName());
        Assertions.assertEquals(task3.getDescription(), body.getDescription());
        Assertions.assertEquals(task3.getRepositoryPath(), body.getRepositoryPath());
    }

    @Test
    void admin_should_be_able_to_save_new_task() {
        //given
        Task task5 = new Task(
                "5",
                "Task Name 5",
                "Task description 5",
                "https://github.com/some-reporitory-5"
        );
        String adminAccessToken = getTokenForAdmin();

        //when
        ResponseEntity<TaskDto> response = callSaveTask(task5, adminAccessToken);

        //then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        //and
        TaskDto body = response.getBody();
        Assertions.assertEquals(task5.getId(), body.getId());
        Assertions.assertEquals(task5.getName(), body.getName());
        Assertions.assertEquals(task5.getDescription(), body.getDescription());
        Assertions.assertEquals(task5.getRepositoryPath(), body.getRepositoryPath());
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
        Task task5 = new Task(
                "5",
                "Task Name 5",
                "Task description 5",
                "/path/xxx"
        );
        userService.saveUser(user);
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());
        //when
        ResponseEntity<TaskDto> response = callSaveTask(task5, token);
        //then
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    void should_return_conflict_about_duplicated_task(){
        //given
        Task task9 = new Task(
                "9",
                "Task Name 9",
                "Task description 9",
                "/repo/path"
        );
        String adminAccessToken = getTokenForAdmin();
        taskService.saveTask(task9);
        //when
        ResponseEntity<TaskDto> response = callSaveTask(task9,adminAccessToken);
        //then
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CONFLICT);
    }

    @Test
    void admin_should_be_able_to_delete_task() {
        //given
        Task task6 = new Task(
                "6",
                "Task Name 6",
                "Task description 6",
                "/repo/path"
        );
        String adminAccessToken = getTokenForAdmin();
        taskService.saveTask(task6);
        callDeleteTask(task6, adminAccessToken);
        //when
        Exception exception = assertThrows(RecordNotFoundException.class, () -> {
            taskService.getTask(task6.getId());
        });
        //then
        Assertions.assertEquals("Task already exists",exception.getMessage());
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
        Task task6 = new Task(
                "6",
                "Task Name 6",
                "Task description 6",
                "/path/path"
        );
        userService.saveUser(user);
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());
        taskService.saveTask(task6);

        //when
        ResponseEntity<TaskDto> response = callDeleteTask(task6, token);

        //then
        Assertions.assertEquals(response.getStatusCode(),HttpStatus.FORBIDDEN);
    }

    @Test
    void admin_should_be_able_to_update_task() {
        //given
        Task task7 = new Task(
                "7",
                "Task Name 7",
                "Task description 7",
                "/new/path"
        );
        Task updatedTask = new Task(
                "7",
                "Task Name 7 is updated",
                "Task 7 description is updated ",
                "/no/idea/path"
        );
        String adminAccessToken = getTokenForAdmin();
        taskService.saveTask(task7);
        //when
        ResponseEntity<TaskDto> response = callUpdateTask(updatedTask, adminAccessToken);
        //then
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        //and
        TaskDto body = response.getBody();
        Assertions.assertEquals(task7.getId(), body.getId());
        Assertions.assertEquals(updatedTask.getName(), body.getName());
        Assertions.assertEquals(updatedTask.getDescription(), body.getDescription());
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

    private ResponseEntity<TaskDto> callSaveTask(Task body, String accessToken) {
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

    private ResponseEntity<TaskDto> callDeleteTask(Task body, String accessToken) {
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

    private ResponseEntity<TaskDto> callUpdateTask(Task body, String accessToken) {
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
