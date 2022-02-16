package pl.sages.javadevpro.projecttwo.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import pl.sages.javadevpro.projecttwo.domain.exception.RecordNotFoundException;
import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.usertask.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log
@RequiredArgsConstructor
public class UserTaskService {

    private final GitService gitService;
    private final DirectoryService directoryService;
    private final UserService userService;
    private final TaskService taskService;
    private final UserTaskExecutor userTaskExecutor;

    public String exec(String userId, String taskId) {
        User user = userService.getUserById(userId);
        List<UserTask> tasks = user.getTasks();
        if (tasks == null) {
            throw new RecordNotFoundException("Task is not assigned to user");
        }
        UserTask taskToSend = tasks.stream()
            .filter(task -> task.getId().equals(taskId))
            .findFirst()
            .orElseThrow(() -> new RecordNotFoundException("Task is not assigned to user"));
            taskToSend.setTaskStatus(TaskStatus.SUBMITTED);
        updateUserTaskInDB(taskToSend, user);
        return userTaskExecutor.exec(taskToSend);
    }

    public UserTask assignTask(String userId, String taskId) {
        User user = userService.getUserById(userId);
        Task task = taskService.getTask(taskId);

        UserTask userTask;
        userTask = createFromTask(task, user.getEmail());

        addUserTaskToDB(userTask, user);
        return userTask;
    }

    public List<String> readListOfAvailableFilesForUserTask (String userId, String taskId) {
        return directoryService.readListOfAvailableFilesForUserTask(userId, taskId);
    }

    public void uploadFileForUserTask(String userEmail, String taskId, String fileId, byte[] bytes) {
        directoryService.uploadFileForUserTask(userEmail, taskId, fileId, bytes);
    }

    public File takeFileFromUserTask(String userEmail, String taskId, String fileId) {
        return directoryService.takeFileFromUserTask(userEmail, taskId, fileId);
    }

    public void commitTask(String userId, String taskId) {
        gitService.commitTask(directoryService.getPathToUserTask(userId, taskId));
    }

    public String getUserTaskStatusSummary(String userId, String taskId) {
        File resultFile = directoryService.getResultFile(userId, taskId);
        try {
            return Files.readAllLines(resultFile.toPath()).stream().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            log.warning(e.getMessage());
            throw new RecordNotFoundException("Not found");
        }
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

    private UserTask createFromTask(Task task, String userId) {
        UserTask userTask = new UserTask();
        userTask.setUserTaskFolder(copyRepositoryToUserFolder(task, userId));
        userTask.setId(task.getId());
        userTask.setName(task.getName());
        userTask.setDescription(task.getDescription());
        userTask.setTaskStatus(TaskStatus.NOT_STARTED);
        userTask.setUserId(userId);
        return userTask;
    }

    private void addUserTaskToDB(UserTask userTask, User user) {
        if (user.getTasks() == null) {
            user.setTasks(new ArrayList<>());
        }
        user.getTasks().add(userTask);
        userService.updateUser(user);
    }

    private String copyRepositoryToUserFolder(Task task, String userId) {
        String destinationFolderPath = directoryService.createDirectoryForUserTask(task, userId);
        gitService.cloneTask(task.getRepositoryPath(), destinationFolderPath);
        return destinationFolderPath;
    }

}
