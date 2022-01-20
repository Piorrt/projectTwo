package pl.sages.javadevpro.projecttwo.domain;

import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.errors.JGitInternalException;
import pl.sages.javadevpro.projecttwo.domain.exception.DuplicateRecordException;
import pl.sages.javadevpro.projecttwo.domain.exception.RecordNotFoundException;
import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.usertask.*;

import java.util.Optional;

@RequiredArgsConstructor
public class UserTaskService {

    private final GitService gitService;
    private final DirectoryService directoryService;
    private final UserService userService;
    private final TaskService taskService;
    private final UserTaskExecutor userTaskExecutor;


    public String exec(String email, String id) {
        User user = userService.getUser(email);
        UserTask userTask = user.getTasks().stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RecordNotFoundException("Task is not assigned to user"));

        return userTaskExecutor.exec(userTask);
    }

    public UserTask assignTask(String userEmail, String taskId) {
        User user = userService.getUser(userEmail);
        Task task = taskService.getTask(taskId);

        UserTask userTask;
        try {
            userTask = createFromTask(task, user.getEmail());
        } catch (JGitInternalException e) {
            throw new DuplicateRecordException("Task " + task.getId() + " was already assigned to user " + user.getEmail());
        }

        addUserTaskToDB(userTask, user);
        return userTask;
    }

    private UserTask createFromTask(Task task, String userEmail) {
        UserTask userTask = new UserTask();
        userTask.setUserTaskFolder(copyRepositoryToUserFolder(task, userEmail));
        userTask.setId(task.getId());
        userTask.setName(task.getName());
        userTask.setDescription(task.getDescription());
        userTask.setTaskStatus(TaskStatus.NOT_STARTED);
        userTask.setUserEmail(userEmail);
        return userTask;
    }

    private void addUserTaskToDB(UserTask userTask, User user) {
        user.getTasks().add(userTask);
        userService.updateUser(user);
    }

    private String copyRepositoryToUserFolder(Task task, String userEmail) {
        String destinationFolderPath = directoryService.createDirectoryForUserTask(task, userEmail);
        gitService.cloneTask(task.getRepositoryPath(), destinationFolderPath);
        return destinationFolderPath;
    }

}
