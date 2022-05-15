package pl.sages.javadevpro.projecttwo;

import pl.sages.javadevpro.projecttwo.domain.user.model.User;
import pl.sages.javadevpro.projecttwo.domain.user.model.UserRole;

import java.util.Set;

public class TestUserFactory {

    private static int userSequence = 0;

    public static User createStudent() {
        userSequence++;
        return new User(
                "TEST" + userSequence,
                "newUser" + userSequence + "@example.com",
                "User Name " + userSequence,
                "password",
                Set.of(UserRole.STUDENT)
                );
    }

    public static User createAdmin() {
        userSequence++;
        return new User(
            "TEST" + userSequence,
            "newUser" + userSequence + "@example.com",
            "User Name " + userSequence,
            "password",
            Set.of(UserRole.ADMIN)
        );
    }
}
