package pl.sages.javadevpro.projecttwo.domain.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    void update(User user);

    void remove(String id);

    Optional<User> findByEmail(String email);

    Optional<User> findById(String id);

    Page<User> findAll(Pageable pageable);

}
