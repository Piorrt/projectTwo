package pl.sages.javadevpro.projecttwo.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.sages.javadevpro.projecttwo.BaseIT;
import pl.sages.javadevpro.projecttwo.domain.user.User;

import java.util.ArrayList;
import java.util.List;

public class UserServiceIT extends BaseIT {

    @Autowired
    UserService service;

    @Test
    public void add_user_test() {
        //given
        User user = new User(
                "2T",
                "newUser@example.com",
                "User Name",
                "pass",
                List.of("STUDENT"),
                new ArrayList<>()
        );
        service.saveUser(user);

        //when
        User readUser = service.getUserById(user.getId());

        //then
        Assertions.assertEquals(user.getId(), readUser.getId());
        Assertions.assertEquals(user.getEmail(), readUser.getEmail());
        Assertions.assertEquals(user.getName(), readUser.getName());
        Assertions.assertEquals(user.getPassword(), readUser.getPassword());
        Assertions.assertNotEquals(user, readUser);
    }

    @Test
    public void get_id_should_return_correct_user() {
        //given
        User user1 = new User(
                "4T",
                "newUser1@example.com",
                "User Name 1",
                "pass1",
                List.of("STUDENT"),
                new ArrayList<>()
        );
        User user2 = new User(
                "3T",
                "newUser2@example.com",
                "User Name 2",
                "pass2",
                List.of("STUDENT"),
                new ArrayList<>()
        );
        User user3 = new User(
                "5T",
                "newUser3@example.com",
                "User Name 3",
                "pass3",
                List.of("STUDENT"),
                new ArrayList<>()
        );
        service.saveUser(user1);
        service.saveUser(user2);
        service.saveUser(user3);

        //when
        User readUser = service.getUserById(user2.getId());

        //then
        Assertions.assertEquals(user2.getId(), readUser.getId());
        Assertions.assertEquals(user2.getEmail(), readUser.getEmail());
        Assertions.assertEquals(user2.getName(), readUser.getName());
        Assertions.assertEquals(user2.getPassword(), readUser.getPassword());
        Assertions.assertNotEquals(user2, readUser);
    }

}
