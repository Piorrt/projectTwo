package pl.sages.javadevpro.projecttwo.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import pl.sages.javadevpro.projecttwo.BaseIT;
import pl.sages.javadevpro.projecttwo.TestTaskBlueprintFactory;
import pl.sages.javadevpro.projecttwo.TestUserFactory;
import pl.sages.javadevpro.projecttwo.api.usertask.ErrorResponse;
import pl.sages.javadevpro.projecttwo.api.usertask.ListOfFilesResponse;
import pl.sages.javadevpro.projecttwo.domain.assigment.Assigment;
import pl.sages.javadevpro.projecttwo.domain.assigment.AssigmentService;
import pl.sages.javadevpro.projecttwo.domain.task.TaskBlueprint;
import pl.sages.javadevpro.projecttwo.domain.task.TaskBlueprintService;
import pl.sages.javadevpro.projecttwo.domain.user.User;


public class TaskControllerIT extends BaseIT {

    @Autowired
    private TaskBlueprintService taskBlueprintService;

    @Autowired
    private AssigmentService assigmentService;

    private final User user = TestUserFactory.createStudent();
    private final TaskBlueprint taskBlueprintOne = TestTaskBlueprintFactory.createWithValidUrl("https://github.com/rafal-nowak/task1");

    @BeforeEach
    void prepareRecords() {
        userService.save(user);
        taskBlueprintService.save(taskBlueprintOne);
    }


    @Test
    void user_should_be_able_to_view_own_task_files() {
        Assigment assigment = assigmentService.assignNewTask(user.getId(), taskBlueprintOne.getId());
        String userToken = getAccessTokenForUser(user.getEmail(), user.getPassword());
        String taskId =  assigment.getTaskId();
        //when
        var response = callHttpMethod(
                HttpMethod.GET,
                "/tasks/" + taskId + "/files",
                userToken,
                null,
                ListOfFilesResponse.class
        );

        //then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void user_should_not_be_able_to_view_files_of_task_not_assigned() {
        String userToken = getAccessTokenForUser(user.getEmail(), user.getPassword());
        String taskId =  "notAssignedTaskID";
        //when
        var response = callHttpMethod(
                HttpMethod.GET,
                "/tasks/" + taskId + "/files",
                userToken,
                null,
                ErrorResponse.class
        );


        //then
        Assertions.assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
//        Assertions.assertEquals("ERROR", response.getBody().getStatus());
    }
}
