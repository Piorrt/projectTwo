package pl.sages.javadevpro.projecttwo.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import pl.sages.javadevpro.projecttwo.BaseIT;
import pl.sages.javadevpro.projecttwo.TestTaskBlueprintFactory;
import pl.sages.javadevpro.projecttwo.TestUserFactory;
import pl.sages.javadevpro.projecttwo.api.task.blueprint.TaskBlueprintDto;
import pl.sages.javadevpro.projecttwo.api.task.blueprint.TaskBlueprintDtoMapper;
import pl.sages.javadevpro.projecttwo.api.usertask.ErrorResponse;
import pl.sages.javadevpro.projecttwo.domain.task.TaskBlueprint;
import pl.sages.javadevpro.projecttwo.domain.task.TaskBlueprintService;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.user.UserService;


public class TaskBlueprintControllerIT extends BaseIT {

    @Autowired
    UserService userService;
    @Autowired
    TaskBlueprintService taskBlueprintService;
    @Autowired
    TaskBlueprintDtoMapper mapper;

    @Test
    void should_get_information_about_task() {
        //given
        User user = TestUserFactory.createStudent();
        TaskBlueprint taskBlueprint = TestTaskBlueprintFactory.createRandom();
        userService.save(user);
        taskBlueprintService.save(taskBlueprint);
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());

        //when
        var response = callHttpMethod(HttpMethod.GET,
                "/task-blueprints/" + taskBlueprint.getId(),
                token,
                null,
                TaskBlueprintDto.class);

        //then
        TaskBlueprintDto body = response.getBody();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        //and
        Assertions.assertNotNull(body);
        Assertions.assertEquals(taskBlueprint.getId(), body.getId());
        Assertions.assertEquals(taskBlueprint.getName(), body.getName());
        Assertions.assertEquals(taskBlueprint.getDescription(), body.getDescription());
        Assertions.assertEquals(taskBlueprint.getRepositoryUrl(),body.getRepositoryUrl());
    }

    @Test
    void should_get_information_about_correct_task() {
        //given
        User user = TestUserFactory.createStudent();
        TaskBlueprint taskBlueprint2 = TestTaskBlueprintFactory.createRandom();
        TaskBlueprint taskBlueprint3 = TestTaskBlueprintFactory.createRandom();
        TaskBlueprint taskBlueprint4 = TestTaskBlueprintFactory.createRandom();
        userService.save(user);
        taskBlueprintService.save(taskBlueprint2);
        taskBlueprintService.save(taskBlueprint3);
        taskBlueprintService.save(taskBlueprint4);
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());

        //when
        var response = callHttpMethod(HttpMethod.GET,
                "/task-blueprints/" + taskBlueprint3.getId(),
                token,
                null,
                TaskBlueprintDto.class);

        //then
        TaskBlueprintDto body = response.getBody();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        //and
        Assertions.assertNotNull(body);
        Assertions.assertEquals(taskBlueprint3.getId(), body.getId());
        Assertions.assertEquals(taskBlueprint3.getName(), body.getName());
        Assertions.assertEquals(taskBlueprint3.getDescription(), body.getDescription());
        Assertions.assertEquals(taskBlueprint3.getRepositoryUrl(), body.getRepositoryUrl());
    }

    @Test
     void admin_should_be_able_to_save_new_task() {
        //given
        TaskBlueprint taskBlueprint = TestTaskBlueprintFactory.createRandom();
        String adminAccessToken = getTokenForAdmin();
        //when
        var response = callHttpMethod(HttpMethod.POST,
                "/task-blueprints",
                adminAccessToken,
                taskBlueprint,
                TaskBlueprintDto.class);

        //then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        //and
        TaskBlueprintDto body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertEquals(taskBlueprint.getId(), body.getId());
        Assertions.assertEquals(taskBlueprint.getName(), body.getName());
        Assertions.assertEquals(taskBlueprint.getDescription(), body.getDescription());
        Assertions.assertEquals(taskBlueprint.getRepositoryUrl(), body.getRepositoryUrl());
    }

    @Test
    void student_should_not_be_able_to_save_new_task() {
        //given
        User user = TestUserFactory.createStudent();
        TaskBlueprint taskBlueprint = TestTaskBlueprintFactory.createRandom();
        userService.save(user);
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());

        //when
        var response = callHttpMethod(HttpMethod.POST,
                "/task-blueprints",
                token,
                taskBlueprint,
                ErrorResponse.class);

        //then
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    void should_return_conflict_about_duplicated_task(){
        //given
        TaskBlueprint taskBlueprint = TestTaskBlueprintFactory.createRandom();
        String adminAccessToken = getTokenForAdmin();
        taskBlueprintService.save(taskBlueprint);
        //when
        var response = callHttpMethod(HttpMethod.POST,
                "/task-blueprints",
                adminAccessToken,
                taskBlueprint,
                ErrorResponse.class);
        //then
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CONFLICT);
    }

    @Test
    void admin_should_be_able_to_delete_task() {
        //given
        TaskBlueprint taskBlueprint = TestTaskBlueprintFactory.createRandom();
        String adminAccessToken = getTokenForAdmin();
        taskBlueprintService.save(taskBlueprint);
        //when
        var response = callHttpMethod(HttpMethod.DELETE,
                "/task-blueprints",
                adminAccessToken,
                mapper.toDto(taskBlueprint),
                Void.class);
        //then
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void student_should_not_be_able_to_delete_task() {
        //given
        User user = TestUserFactory.createStudent();
        TaskBlueprint taskBlueprint = TestTaskBlueprintFactory.createRandom();
        userService.save(user);
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());
        taskBlueprintService.save(taskBlueprint);

        //when
        var response = callHttpMethod(
            HttpMethod.DELETE,
            "/task-blueprints",
            token,
            taskBlueprint,
            ErrorResponse.class
        );

        //then
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    void admin_should_be_able_to_update_task() {
        //given
        TaskBlueprint taskBlueprint = TestTaskBlueprintFactory.createRandom();
        TaskBlueprint updatedTaskBlueprint = new TaskBlueprint(
                taskBlueprint.getId(),
                "Task Name is updated",
                "Task description is updated ",
                "/no/idea/path/updated"
        );
        String adminAccessToken = getTokenForAdmin();
        taskBlueprintService.save(taskBlueprint);

        //when
        var response = callHttpMethod(HttpMethod.PUT,
                "/task-blueprints",
                adminAccessToken,
                updatedTaskBlueprint,
                TaskBlueprintDto.class);

        //then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        //and
        TaskBlueprintDto body = response.getBody();
        Assertions.assertNull(body);
    }

    @Test
    void should_get_response_code_404_when_task_not_exits() {
        //given
        User user = TestUserFactory.createStudent();
        userService.save(user);
        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());
        //when
        var response = callHttpMethod(HttpMethod.GET,
                "/task-blueprints/1",
                token,
                null,
                ErrorResponse.class);

        //then
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

}
