package pl.sages.javadevpro.projecttwo.domain.usertask;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserTask {

    private String id;
    private String name;
    private String description;
    private String userTaskFolder;
    private TaskStatus taskStatus;
    private String userEmail;

}
