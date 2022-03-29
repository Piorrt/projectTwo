package pl.sages.javadevpro.projecttwo.external.storage.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DuplicateKeyException;
import pl.sages.javadevpro.projecttwo.domain.task.TaskBlueprint;
import pl.sages.javadevpro.projecttwo.domain.task.TaskBlueprintRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Log
public class TaskBlueprintStorageAdapter implements TaskBlueprintRepository {
    private final TaskBlueprintMongoRepository taskRepository;
    private final TaskBlueprintEntityMapper mapper;

    @Override
    public TaskBlueprint save(TaskBlueprint taskBlueprint) {
        try{
            TaskBlueprintEntity saved = taskRepository.insert(mapper.toEntity(taskBlueprint));
            log.info("Saved task blueprint" + saved);
            return mapper.toDomain(saved);
        }catch (DuplicateKeyException ex){
            log.warning("Task blueprint " +  taskBlueprint.getName() + " already exits");
            throw new TaskBlueprintAlreadyExistsException("Task blueprint already exists");
        }
    }

    @Override
    public Optional<TaskBlueprint> findById(String id) {
        return taskRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void remove(TaskBlueprint taskBlueprint) {
        taskRepository.deleteById(taskBlueprint.getId());
    }

    @Override
    public void update(TaskBlueprint updatedTaskBlueprint) {
        TaskBlueprintEntity updated = taskRepository.save(mapper.toEntity(updatedTaskBlueprint));
        log.info("Updating task blueprint"+ updated);
    }
}
