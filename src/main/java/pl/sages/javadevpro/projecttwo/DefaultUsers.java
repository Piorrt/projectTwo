package pl.sages.javadevpro.projecttwo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.user.UserRole;
import pl.sages.javadevpro.projecttwo.domain.user.UserService;

import java.util.Set;

@Component
public class DefaultUsers implements CommandLineRunner {

    private final UserService userService;

    public DefaultUsers(UserService userService) {
        this.userService = userService;
    }

    private final User adminUser = new User(
        null,
        "jan@example.com",
        "Jan Kowalski",
        "MyPassword",
        Set.of(UserRole.ADMIN)
    );

    private final User studentUser = new User(
        null,
        "stefan@example.com",
        "Stefan Burczymucha",
        "password",
        Set.of(UserRole.STUDENT)
    );

    @Override
    public void run(String... args) throws Exception {
        try {
            addUser(adminUser);
            addUser(studentUser);
        } catch (Exception ex) {

        }
    }

    private void addUser(User user) {
        userService.save(user);
    }
}