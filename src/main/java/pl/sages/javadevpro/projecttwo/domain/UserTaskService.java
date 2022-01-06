package pl.sages.javadevpro.projecttwo.domain;

import lombok.RequiredArgsConstructor;
import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.usertask.DirectoryHandler;
import pl.sages.javadevpro.projecttwo.domain.usertask.GitService;
import pl.sages.javadevpro.projecttwo.domain.usertask.TaskStatus;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;

@RequiredArgsConstructor
public class UserTaskService {

    private final GitService gitService;
    private final DirectoryHandler directoryHandler;
    private final UserService userService;

    public UserTask assignTask(Task task, String userEmail) {
        UserTask userTask = createFromTask(task, userEmail);
        addUserTaskToDB(userTask);
        return userTask;
    }

    private UserTask createFromTask(Task task, String userEmail) {
        UserTask userTask = new UserTask();
        userTask.setId(task.getId());
        userTask.setName(task.getName());
        userTask.setDescription(task.getDescription());
        try {
            userTask.setUserTaskFolder(copyRepositoryToUserFolder(task, userEmail));
        } catch (Exception e) {
            e.printStackTrace();
        }
        userTask.setTaskStatus(TaskStatus.NOT_STARTED);
        return userTask;
    }

    private void addUserTaskToDB(UserTask userTask) {
        User user = userService.getUser(userTask.getUserEmail());
        user.getTasks().add(userTask);
        userService.saveUser(user);
    }

    private String copyRepositoryToUserFolder(Task task, String userEmail) {
        String destinationFolderPath = directoryHandler.createDirectoryForUserTask(task, userEmail);
        gitService.cloneTask(task.getRepositoryPath(), destinationFolderPath);
        return destinationFolderPath;
    }







}
