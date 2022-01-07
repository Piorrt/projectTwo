package pl.sages.javadevpro.projecttwo.external.storage.usertask;

import lombok.Setter;
import pl.sages.javadevpro.projecttwo.domain.usertask.TaskStatus;

@Setter
public class UserTaskEntity {

    private String id;
    private String name;
    private String description;
    private String userTaskFolder;
    private TaskStatus taskStatus;
    private String userEmail;

}
