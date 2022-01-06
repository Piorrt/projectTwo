package pl.sages.javadevpro.projecttwo.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.sages.javadevpro.projecttwo.BaseIT;
import pl.sages.javadevpro.projecttwo.domain.task.Task;

public class TaskServiceIT extends BaseIT {

    @Autowired
    TaskService service;

    @Test
    public void add_task_test() {
        //given
        Task task = new Task(
                "1",
                "Task Name 1",
                "Task Description 1",
                "https://github.com/some-reporitory-1"
        );
        service.saveTask(task);

        //when
        Task readTask = service.getTask(task.getId());

        //then
        Assertions.assertEquals(task.getId(), readTask.getId());
        Assertions.assertEquals(task.getName(), readTask.getName());
        Assertions.assertEquals(task.getDescription(), readTask.getDescription());
        Assertions.assertEquals(task.getRepositoryPath(), readTask.getRepositoryPath());
        Assertions.assertNotEquals(task, readTask);
    }

    @Test
    public void get_email_should_return_correct_user() {
        //given
        Task task1 = new Task(
                "2",
                "Task Name 2",
                "Task Description 2",
                "https://github.com/some-reporitory-2"
        );
        Task task2 = new Task(
                "3",
                "Task Name 3",
                "Task Description 3",
                "https://github.com/some-reporitory-3"
        );
        Task task3 = new Task(
                "4",
                "Task Name 4",
                "Task Description 4",
                "https://github.com/some-reporitory-4"
        );
        service.saveTask(task1);
        service.saveTask(task2);
        service.saveTask(task3);

        //when
        Task readTask = service.getTask(task2.getId());

        //then
        Assertions.assertEquals(task2.getId(), readTask.getId());
        Assertions.assertEquals(task2.getName(), readTask.getName());
        Assertions.assertEquals(task2.getDescription(), readTask.getDescription());
        Assertions.assertEquals(task2.getRepositoryPath(), readTask.getRepositoryPath());
        Assertions.assertNotEquals(task2, readTask);
    }

}
