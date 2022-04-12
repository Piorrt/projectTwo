package pl.sages.javadevpro.projecttwo.domain.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    void update(User user);

    void remove(String id);

    Optional<User> findByEmail(String email);

    Optional<User> findById(String id);

    List<User> findAll();

}
