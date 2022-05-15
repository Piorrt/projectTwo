package pl.sages.javadevpro.projecttwo.domain.user;

import org.springframework.data.domain.Pageable;
import pl.sages.javadevpro.projecttwo.domain.user.model.PageUser;
import pl.sages.javadevpro.projecttwo.domain.user.model.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    void update(User user);

    void remove(String id);

    Optional<User> findByEmail(String email);

    Optional<User> findById(String id);

    PageUser findAll(Pageable pageable);

}