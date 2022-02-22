package pl.sages.javadevpro.projecttwo.external.storage.user;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@TypeAlias("Users")
@Document(value = "UserEntity")
@Builder
public class UserEntity {

    @Id
    private String id;
    private String email;
    private String name;
    private String password;
    private List<String> roles;


}
