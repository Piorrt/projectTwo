package pl.sages.javadevpro.projecttwo.domain.usertask;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserTaskRequest {

    private String userEmail;
    private String taskId;
}
