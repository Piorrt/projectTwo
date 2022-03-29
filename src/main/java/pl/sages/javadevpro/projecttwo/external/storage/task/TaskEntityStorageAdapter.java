package pl.sages.javadevpro.projecttwo.external.storage.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DuplicateKeyException;
import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.domain.task.TaskRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Log
public class TaskEntityStorageAdapter implements TaskRepository {

    private final TaskMongoRepository taskRepository;
    private final TaskEntityMapper mapper;

    @Override
    public Task save(Task task) {
        try{
            TaskEntity saved = taskRepository.insert(mapper.toEntity(task));
            log.info("Saved task " + saved);
            return mapper.toDomain(saved);
        }catch (DuplicateKeyException ex){
            log.warning("Task " +  task.getName() + " already exits");
            throw new TaskAlreadyExistsException();
        }
    }

    @Override
    public Optional<Task> findById(String id) {
        return taskRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void update(Task updatedTask) {
        TaskEntity updated = taskRepository.save(mapper.toEntity(updatedTask));
        log.info("Updating task "+ updated);
    }
}
