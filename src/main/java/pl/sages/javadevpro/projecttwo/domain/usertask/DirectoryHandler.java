package pl.sages.javadevpro.projecttwo.domain.usertask;

import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.domain.user.User;

public interface DirectoryHandler {

    String createDirectoryForUserTask(Task task, String userEmail);

}
