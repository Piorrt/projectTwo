package pl.sages.javadevpro.projecttwo.domain.task;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static pl.sages.javadevpro.projecttwo.domain.task.TaskStatus.NOT_STARTED;
import static pl.sages.javadevpro.projecttwo.domain.task.TaskStatus.SUBMITTED;

@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final Workspace taskWorkspace;
    private final TaskBlueprintService taskBlueprintService;
    private final TaskExecutor taskExecutor;
    private final String resultFilePath;

    public String execute(String taskId) {
        Task task = updateTaskStatus(taskId, SUBMITTED);
        taskExecutor.exec(task);
        return task.getStatus().name();
    }

    public List<String> getTaskFilesList(String taskId) {
        return taskWorkspace.getFilesList(getWorkspacePath(taskId));
    }

    public byte[] readTaskFile(String taskId, String filePath) {
        return taskWorkspace.readFile(getWorkspacePath(taskId), filePath);
    }

    public void commitTaskChanges(String taskId) {
        taskWorkspace.commitChanges(getWorkspacePath(taskId));
    }

    public byte[] readTaskResults(String taskId) {
        return readTaskFile(taskId, resultFilePath);
    }

    public Task updateTaskStatus(String taskId, TaskStatus newStatus) {
        var task = findTaskById(taskId).withStatus(newStatus);
        taskRepository.update(task);
        return task;
    }

    public Task createTask(String blueprintId) {
        var blueprint = taskBlueprintService.findBy(blueprintId);
        var workspaceUrl = taskWorkspace.createWorkspace(blueprint.getRepositoryUrl());
        var task = Task.builder()
                .name(blueprint.getName())
                .description(blueprint.getDescription())
                .workspaceUrl(workspaceUrl)
                .status(NOT_STARTED)
                .build();
        return taskRepository.save(task);
    }

    public TaskStatus getTaskStatus(String taskId) {
        return findTaskById(taskId).getStatus();
    }

    private Task findTaskById(String taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);
    }

    private String getWorkspacePath(String taskId) {
        return findTaskById(taskId).getWorkspaceUrl();
    }

    public void writeAndCommitTask(String taskId, int fileId, MultipartFile file) {
       // try {
        String filePath = getTaskFilesList(taskId).get(fileId);
            taskWorkspace.writeFile(getWorkspacePath(taskId), filePath, file);
            commitTaskChanges(taskId);
        //} catch (IOException e) {
           // throw new CommitTaskException();
       // }
    }

    private void writeTaskFile(String taskId, String filePath, MultipartFile file) {

    }
}
