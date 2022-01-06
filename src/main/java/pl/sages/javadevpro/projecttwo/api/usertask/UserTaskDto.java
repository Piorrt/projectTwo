package pl.sages.javadevpro.projecttwo.api.usertask;

import lombok.Setter;
import pl.sages.javadevpro.projecttwo.domain.usertask.TaskStatus;

@Setter
public class UserTaskDto {

    private String id;
    private String name;
    private String description;
    private TaskStatus taskStatus;

}
