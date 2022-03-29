package pl.sages.javadevpro.projecttwo.domain.user;

import lombok.*;

import java.util.List;

@Value
public class User {

    String id;
    String email;
    String name;
    String password;
    //todo set zamiast List
    List<UserRole> roles;

    public User withPassword(String newPassword) {
        return new User(id, email, name, newPassword, roles);
    }
}
