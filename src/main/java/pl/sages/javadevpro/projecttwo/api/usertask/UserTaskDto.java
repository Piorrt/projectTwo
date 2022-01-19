package pl.sages.javadevpro.projecttwo.api.usertask;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.sages.javadevpro.projecttwo.domain.usertask.TaskStatus;

@Getter
@Setter
@AllArgsConstructor
public class UserTaskDto {

    private String id;
    private String name;
    private String description;
    private TaskStatus taskStatus;

}
