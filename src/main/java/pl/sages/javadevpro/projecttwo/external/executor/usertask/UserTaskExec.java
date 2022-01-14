package pl.sages.javadevpro.projecttwo.external.executor.usertask;

import lombok.Data;

@Data
public class UserTaskExec {

    private final String taskId;
    private final String userId;
    private final String taskPath;
    private final String taskStatus;

}

