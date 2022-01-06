package pl.sages.javadevpro.projecttwo.domain.usertask;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserTask {

    private String id;
    private String name;
    private String description;
    private String userTaskFolder;
    private TaskStatus taskStatus;
    private String userEmail;

}
