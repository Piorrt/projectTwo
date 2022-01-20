package pl.sages.javadevpro.projecttwo.domain.user;

import lombok.*;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;

import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private String email;
    private String name;
    private String password;
    private List<String> roles;
    private List<UserTask> tasks = new ArrayList<>();

}
