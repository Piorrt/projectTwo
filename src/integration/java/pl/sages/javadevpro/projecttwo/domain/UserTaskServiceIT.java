package pl.sages.javadevpro.projecttwo.domain;

import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.sages.javadevpro.projecttwo.BaseIT;
import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserTaskServiceIT extends BaseIT {

    @Autowired
    UserTaskService userTaskService;

    @Autowired
    UserService userService;

    @Test
    void user_should_have_new_task_after_assignment() {
        User user = new User(
                "newUser12@example.com",
                "User Name 12",
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

        //when
        UserTask userTask = userTaskService.createFromTask(task,user.getEmail());
        userTaskService.addUserTaskToDB(userTask, user);
        User backFromDB = userService.getUser(user.getEmail());
        List<UserTask> tasks = backFromDB.getTasks();

        //then
        Assertions.assertFalse(tasks.isEmpty());
        Assertions.assertEquals(1, tasks.size());
    }

    @SneakyThrows
    @AfterAll
    private static void cleanUpFolders(){
        File file = new File("userTasks");
        FileUtils.cleanDirectory(file);
    }

}
