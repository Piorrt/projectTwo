package pl.sages.javadevpro.projecttwo.external.env.usertask;

import lombok.Data;

@Data
public class UserTaskEnv {

    private final String taskId;
    private final String userId;
    private final String taskPath;
    private final String taskStatus;

}

