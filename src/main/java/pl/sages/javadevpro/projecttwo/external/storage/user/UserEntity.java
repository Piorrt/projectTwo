package pl.sages.javadevpro.projecttwo.external.storage.user;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.sages.javadevpro.projecttwo.external.storage.usertask.UserTaskEntity;


import java.util.ArrayList;
import java.util.List;

@Data
@TypeAlias("Users")
@Document(value = "UserEntity")
@Builder
public class UserEntity {

    @Id
    private String email;
    private String name;
    private String password;
    private List<String> roles;
    private List<UserTaskEntity> tasks = new ArrayList<>();

}
