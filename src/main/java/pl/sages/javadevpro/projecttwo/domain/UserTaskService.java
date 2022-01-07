package pl.sages.javadevpro.projecttwo.domain;

import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.errors.JGitInternalException;
import pl.sages.javadevpro.projecttwo.domain.exception.DuplicateRecordException;
import pl.sages.javadevpro.projecttwo.domain.exception.RecordNotFoundException;
import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.usertask.DirectoryService;
import pl.sages.javadevpro.projecttwo.domain.usertask.GitService;
import pl.sages.javadevpro.projecttwo.domain.usertask.TaskStatus;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;

@RequiredArgsConstructor
public class UserTaskService {

    private final GitService gitService;
    private final DirectoryService directoryService;
    private final UserService userService;

    public UserTask assignTask(Task task, String userEmail) {
        User user = userService.getUser(userEmail);
        if (user==null) {
            throw new RecordNotFoundException("User does not exist");
        }

        UserTask userTask;
        try {
            userTask = createFromTask(task, userEmail);
        } catch (JGitInternalException e) {
            throw new DuplicateRecordException("Task " + task.getId() + " ias already assigned to user " + userEmail);
        }

        addUserTaskToDB(userTask, user);
        return userTask;
    }

    UserTask createFromTask(Task task, String userEmail) {
        UserTask userTask = new UserTask();
        userTask.setUserTaskFolder(copyRepositoryToUserFolder(task, userEmail));
        userTask.setId(task.getId());
        userTask.setName(task.getName());
        userTask.setDescription(task.getDescription());
        userTask.setTaskStatus(TaskStatus.NOT_STARTED);
        userTask.setUserEmail(userEmail);
        return userTask;
    }

    void addUserTaskToDB(UserTask userTask, User user) {
        user.getTasks().add(userTask);
        userService.updateUser(user);
    }

    private String copyRepositoryToUserFolder(Task task, String userEmail) {
        String destinationFolderPath = directoryService.createDirectoryForUserTask(task, userEmail);
        gitService.cloneTask(task.getRepositoryPath(), destinationFolderPath);
        return destinationFolderPath;
    }






}
