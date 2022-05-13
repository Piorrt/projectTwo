package pl.sages.javadevpro.projecttwo.domain.assigment;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import pl.sages.javadevpro.projecttwo.domain.task.TaskService;


// TODO final + required args constructor - done
@RequiredArgsConstructor
public class AssigmentService {

    private final AssigmentRepository assigmentRepository;
    private final TaskService taskService;

    public Assigment assignNewTask(String userId, String taskBlueprintId) {
        var task = taskService.createTask(taskBlueprintId);
        var assigment = new Assigment(userId, task.getId());
        return assigmentRepository.save(assigment);
    }

    public boolean isTaskAssignedToUser(String userId, String taskId){
       return assigmentRepository.find(userId, taskId).isPresent();
    }
}
