package pl.sages.javadevpro.projecttwo.domain.user;

import lombok.Value;

import java.util.Set;

@Value
public class User {

    String id;
    String email;
    String name;
    String password;
    Set<UserRole> roles;

    public User withPassword(String newPassword) {
        return new User(id, email, name, newPassword, roles);
    }
}