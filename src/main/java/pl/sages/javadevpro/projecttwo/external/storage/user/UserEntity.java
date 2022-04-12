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

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String name;
    private String password;
    private Set<String> roles;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}