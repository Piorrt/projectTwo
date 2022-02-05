package pl.sages.javadevpro.projecttwo.domain;

import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.errors.JGitInternalException;
import pl.sages.javadevpro.projecttwo.domain.exception.DuplicateRecordException;
import pl.sages.javadevpro.projecttwo.domain.exception.RecordNotFoundException;
import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.usertask.*;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class UserTaskService {

    private final GitService gitService;
    private final DirectoryService directoryService;
    private final UserService userService;
    private final TaskService taskService;
    private final UserTaskExecutor userTaskExecutor;

    public String exec(String userEmail, String taskId) {
        User user = userService.getUser(userEmail);
        List<UserTask> tasks = user.getTasks();
        if (tasks == null) {
            throw new RecordNotFoundException("Task is not assigned to user");
        }
        UserTask taskToSend = tasks.stream()
            .filter(task -> task.getId().equals(taskId))
            .findFirst()
            .orElseThrow(() -> new RecordNotFoundException("Task is not assigned to user"));
            taskToSend.setTaskStatus(TaskStatus.STARTED);
        updateUserTaskInDB(taskToSend, user);
        return userTaskExecutor.exec(taskToSend);

/// todo 1. wysylamy prawidlowy folder sciezka bezwzgledna
/// todo 2. zmiana statusu taska na czas wykonania zadania - status - STARTED
/// todo 3. zapis resultatow
        // - commit wyniku   save statusu zadania
        // - FAIlED/COMPLETE
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

    public void updateUserTaskInDB(UserTask userTask, User user) {
        if (user.getTasks() == null) {
            user.setTasks(new ArrayList<>());
        }
        List<UserTask> tasks = user.getTasks();
        int indexOfUserTaskToUpdate = tasks.indexOf(userTask);

        tasks.set(indexOfUserTaskToUpdate, userTask);
        userService.updateUser(user);
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
        if (user.getTasks() == null) {
            user.setTasks(new ArrayList<>());
        }
        user.getTasks().add(userTask);
        userService.updateUser(user);
    }

    private String copyRepositoryToUserFolder(Task task, String userEmail) {
        String destinationFolderPath = directoryService.createDirectoryForUserTask(task, userEmail);
        gitService.cloneTask(task.getRepositoryPath(), destinationFolderPath);
        return destinationFolderPath;
    }

}
