package pl.sages.javadevpro.projecttwo.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sages.javadevpro.projecttwo.BaseIT;
import pl.sages.javadevpro.projecttwo.TestUserFactory;
import pl.sages.javadevpro.projecttwo.domain.user.model.User;
import pl.sages.javadevpro.projecttwo.domain.user.UserService;

public class UserServiceIT extends BaseIT {

    @Autowired
    UserService service;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void add_user_test() {
        //given
        User user = TestUserFactory.createStudent();
        service.save(user);

        //when
        User readUser = service.findById(user.getId());

        //then
        Assertions.assertEquals(user.getEmail(), readUser.getEmail());
        Assertions.assertEquals(user.getName(), readUser.getName());
        Assertions.assertTrue(passwordEncoder.matches(user.getPassword(), readUser.getPassword()));
    }

    @Test
    public void get_id_should_return_correct_user() {
        //given
        User user1 = TestUserFactory.createStudent();
        User user2 = TestUserFactory.createStudent();
        User user3 = TestUserFactory.createStudent();
        service.save(user1);
        service.save(user2);
        service.save(user3);

        //when
        User readUser = service.findById(user2.getId());

        //then
        Assertions.assertEquals(user2.getEmail(), readUser.getEmail());
        Assertions.assertEquals(user2.getName(), readUser.getName());
        Assertions.assertTrue(passwordEncoder.matches(user2.getPassword(), readUser.getPassword()));
    }

}
