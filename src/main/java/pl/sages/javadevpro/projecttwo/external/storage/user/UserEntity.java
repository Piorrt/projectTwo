package pl.sages.javadevpro.projecttwo.external.storage.user;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import pl.sages.javadevpro.projecttwo.external.storage.usertask.UserTaskEntity;

import java.util.List;

@Data
@TypeAlias("Users")
@Document(value = "UserEntity")
@Builder
public class UserEntity {

    @MongoId
    private String id;
    private String email;
    private String name;
    private String password;
    private List<String> roles;
    private List<UserTaskEntity> tasks;

}
