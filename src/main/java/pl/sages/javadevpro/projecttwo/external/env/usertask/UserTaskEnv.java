package pl.sages.javadevpro.projecttwo.external.env.usertask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTaskEnv {

    private String id;
    private String name;
    private String description;
    private String userTaskFolder;
    private UserTaskStatusEnv taskStatus;
    private String userEmail;

}

