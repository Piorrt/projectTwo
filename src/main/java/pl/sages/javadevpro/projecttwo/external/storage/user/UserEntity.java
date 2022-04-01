package pl.sages.javadevpro.projecttwo.external.storage.user;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@TypeAlias("Users")//todo - do sprawdzenie czy nie jest zbędne - @Document.value = Users
@Document(value = "UserEntity")
@Builder
public class UserEntity {

    //todo 18. equals hashCode własna implemntacja oparta (equals - oparte o id)
    @Id
    private String id;
    @Indexed(unique = true)
    private String email;
    private String name;
    private String password;
    private Set<String> roles;
}