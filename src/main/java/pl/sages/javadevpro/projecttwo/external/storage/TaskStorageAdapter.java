package pl.sages.javadevpro.projecttwo.external.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DuplicateKeyException;
import pl.sages.javadevpro.projecttwo.domain.exception.DuplicateRecordException;
import pl.sages.javadevpro.projecttwo.domain.exception.RecordNotFoundException;
import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.domain.task.TaskRepository;
import pl.sages.javadevpro.projecttwo.external.storage.task.MongoTaskRepository;
import pl.sages.javadevpro.projecttwo.external.storage.task.TaskEntity;
import pl.sages.javadevpro.projecttwo.external.storage.task.TaskEntityMapper;

import java.util.Optional;

@RequiredArgsConstructor
@Log
public class TaskStorageAdapter implements TaskRepository {
    private final MongoTaskRepository taskRepository;
    private final TaskEntityMapper mapper;

    @Override
    public Task save(Task task) {
        try{
            TaskEntity saved = taskRepository.insert(mapper.toEntity(task));
            log.info("Saved task " + saved.toString());
            return mapper.toDomain(saved);
        }catch (DuplicateKeyException ex){
            log.warning("Task " +  task.getName() + " already exits");
            throw new DuplicateRecordException("Task already exits");
        }
    }

    @Override
    public Optional<Task> findById(String id) {
        Optional<TaskEntity> entity = taskRepository.findById(id);
        if (entity.isEmpty()) {
            throw new RecordNotFoundException("Task not found");
        }
        log.info("Found task " + entity.map(Object::toString));
        return entity
                .map(mapper::toDomain);
    }

    @Override
    public void remove(String taskId) {
        Optional<Task> task = findById(taskId);
        if(task.isPresent()) {
            log.info("Removing task " + task);
            taskRepository.deleteById(taskId);
        }
    }

    @Override
    public Task update(Task updatedTask) {
        TaskEntity updated = taskRepository.save(mapper.toEntity(updatedTask));
        log.info("Updating task "+ updated);
        return mapper.toDomain(updated);
    }
}
