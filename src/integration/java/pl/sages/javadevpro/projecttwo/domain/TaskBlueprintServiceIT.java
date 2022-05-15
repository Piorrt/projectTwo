package pl.sages.javadevpro.projecttwo.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.sages.javadevpro.projecttwo.BaseIT;
import pl.sages.javadevpro.projecttwo.TestTaskBlueprintFactory;
import pl.sages.javadevpro.projecttwo.domain.task.TaskBlueprint;
import pl.sages.javadevpro.projecttwo.domain.task.TaskBlueprintService;

class TaskBlueprintServiceIT extends BaseIT {

    @Autowired
    TaskBlueprintService service;

    @Test
    void add_task_test() {
        //given
        TaskBlueprint taskBlueprint = TestTaskBlueprintFactory.createRandom();
        service.save(taskBlueprint);

        //when
        TaskBlueprint readTaskBlueprint = service.findBy(taskBlueprint.getId());

        //then
        Assertions.assertEquals(taskBlueprint.getId(), readTaskBlueprint.getId());
        Assertions.assertEquals(taskBlueprint.getName(), readTaskBlueprint.getName());
        Assertions.assertEquals(taskBlueprint.getDescription(), readTaskBlueprint.getDescription());
        Assertions.assertEquals(taskBlueprint.getRepositoryUrl(), readTaskBlueprint.getRepositoryUrl());
//        Assertions.assertNotEquals(taskBlueprint, readTaskBlueprint);
    }

    @Test
    void get_id_should_return_correct_user() {
        //given
        TaskBlueprint taskBlueprint1 = TestTaskBlueprintFactory.createRandom();
        TaskBlueprint taskBlueprint2 = TestTaskBlueprintFactory.createRandom();
        TaskBlueprint taskBlueprint3 = TestTaskBlueprintFactory.createRandom();
        service.save(taskBlueprint1);
        service.save(taskBlueprint2);
        service.save(taskBlueprint3);

        //when
        TaskBlueprint readTaskBlueprint = service.findBy(taskBlueprint2.getId());

        //then
        Assertions.assertEquals(taskBlueprint2.getId(), readTaskBlueprint.getId());
        Assertions.assertEquals(taskBlueprint2.getName(), readTaskBlueprint.getName());
        Assertions.assertEquals(taskBlueprint2.getDescription(), readTaskBlueprint.getDescription());
        Assertions.assertEquals(taskBlueprint2.getRepositoryUrl(), readTaskBlueprint.getRepositoryUrl());

    }

}
