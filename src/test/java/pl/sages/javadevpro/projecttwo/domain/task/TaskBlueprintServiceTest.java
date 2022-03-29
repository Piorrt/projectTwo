package pl.sages.javadevpro.projecttwo.domain.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sages.javadevpro.projecttwo.external.storage.task.TaskBlueprintAlreadyExistsException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskBlueprintServiceTest {

    @Mock
    private TaskBlueprintRepository taskBlueprintRepository;

    @InjectMocks
    private TaskBlueprintService taskBlueprintService;

    private final TaskBlueprint fakeTaskBlueprint = new TaskBlueprint(
            "ID28",
            "task name",
            "task description",
            "github.com"
    );

    @Test
    void save_method_should_return_saved_task_blueprint_when_task_blueprint_does_not_exist() {
        Mockito.when(taskBlueprintRepository.save(fakeTaskBlueprint)).thenReturn(fakeTaskBlueprint);

        //when
        TaskBlueprint savedTaskBlueprint = taskBlueprintService.save(fakeTaskBlueprint);

        //then
        assertNotNull(savedTaskBlueprint);
        assertEquals(fakeTaskBlueprint.getId(), savedTaskBlueprint.getId());
        Assertions.assertEquals(fakeTaskBlueprint.getName(), savedTaskBlueprint.getName());
        assertEquals(fakeTaskBlueprint.getDescription(), savedTaskBlueprint.getDescription());
        Assertions.assertEquals(fakeTaskBlueprint.getRepositoryUrl(), savedTaskBlueprint.getRepositoryUrl());
    }

    @Test
    void save_method_should_throw_task_blueprint_already_exist_exception_when_task_blueprint_exist() {
        Mockito.when(taskBlueprintRepository.save(fakeTaskBlueprint)).thenThrow(new TaskBlueprintAlreadyExistsException());

        //when
        //then
        Assertions.assertThrows(TaskBlueprintAlreadyExistsException.class,
                ()-> taskBlueprintService.save(fakeTaskBlueprint));
    }

    @Test
    void find_by_method_should_return_founded_task_blueprint_when_task_blueprint_exist() {
        Mockito.when(taskBlueprintRepository.findById(fakeTaskBlueprint.getId())).thenReturn(Optional.of(fakeTaskBlueprint));

        //when
        TaskBlueprint foundedTaskBlueprint = taskBlueprintService.findBy(fakeTaskBlueprint.getId());

        //then
        assertNotNull(foundedTaskBlueprint);
        assertEquals(fakeTaskBlueprint.getId(), foundedTaskBlueprint.getId());
        Assertions.assertEquals(fakeTaskBlueprint.getName(), foundedTaskBlueprint.getName());
        assertEquals(fakeTaskBlueprint.getDescription(), foundedTaskBlueprint.getDescription());
        Assertions.assertEquals(fakeTaskBlueprint.getRepositoryUrl(), foundedTaskBlueprint.getRepositoryUrl());
    }

    @Test
    void find_by_method_should_throw_task_blueprint_not_found_exception_when_task_blueprint_does_not_exist() {
        Mockito.when(taskBlueprintRepository.findById(fakeTaskBlueprint.getId())).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThrows(TaskBlueprintNotFoundException.class,
                ()-> taskBlueprintService.findBy(fakeTaskBlueprint.getId()));
    }

    @Test
    void remove_method_should_return_removed_task_blueprint_when_task_blueprint_exist() {
//        Mockito.when(taskBlueprintRepository.remove(fakeTaskBlueprint)).thenReturn(Optional.of(fakeTaskBlueprint));
//
//        //when
//        TaskBlueprint removedTaskBlueprint = taskBlueprintService.remove(fakeTaskBlueprint);
//
//        //then
//        assertNotNull(removedTaskBlueprint);
//        assertEquals(fakeTaskBlueprint.getId(), removedTaskBlueprint.getId());
//        Assertions.assertEquals(fakeTaskBlueprint.getName(), removedTaskBlueprint.getName());
//        assertEquals(fakeTaskBlueprint.getDescription(), removedTaskBlueprint.getDescription());
//        Assertions.assertEquals(fakeTaskBlueprint.getRepositoryUrl(), removedTaskBlueprint.getRepositoryUrl());
    }

    @Test
    void remove_method_should_throw_task_blueprint_not_found_exception_when_task_blueprint_does_not_exist() {
//        Mockito.when(taskBlueprintRepository.remove(fakeTaskBlueprint)).thenReturn(Optional.empty());
//
//        //when
//        //then
//        Assertions.assertThrows(TaskBlueprintNotFoundException.class,
//                ()->{
//                    taskBlueprintService.remove(fakeTaskBlueprint);
//                });
    }

}
