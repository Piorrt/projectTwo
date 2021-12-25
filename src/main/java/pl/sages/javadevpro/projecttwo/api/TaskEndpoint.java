package pl.sages.javadevpro.projecttwo.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sages.javadevpro.projecttwo.api.task.TaskDto;
import pl.sages.javadevpro.projecttwo.api.task.TaskDtoMapper;
import pl.sages.javadevpro.projecttwo.domain.TaskService;
import pl.sages.javadevpro.projecttwo.domain.task.Task;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/tasks")
public class TaskEndpoint {

    private final TaskService taskService;
    private final TaskDtoMapper dtoMapper;

    @GetMapping(
            path = "/{id}",
            produces = "application/json",
            consumes = "application/json"
    )
    @Secured({"ROLE_STUDENT", "ROLE_ADMIN"})
    public ResponseEntity<TaskDto> getTask(@PathVariable(name = "id")int taskId) {
        Task task = taskService.getTask(taskId);

        return ResponseEntity
                .ok(dtoMapper.toDto(task));
    }

    @PostMapping(
            produces = "application/json",
            consumes = "application/json"
    )
    @Secured("ROLE_ADMIN")
    public ResponseEntity<TaskDto> saveTask(@RequestBody TaskDto dto) {
        Task task = taskService.saveTask(dtoMapper.toDomain(dto));
        return ResponseEntity
                .ok(dtoMapper.toDto(task));
    }

}
