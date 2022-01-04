package pl.sages.javadevpro.projecttwo.domain.user;


import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String user);
}
