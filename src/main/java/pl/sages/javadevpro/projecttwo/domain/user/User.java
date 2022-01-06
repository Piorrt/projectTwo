package pl.sages.javadevpro.projecttwo.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private String email;
    private String name;
    private String password;
    private List<String> roles;
    private List<UserTask> tasks;

}
