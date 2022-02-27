package pl.sages.javadevpro.projecttwo.external.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.bson.types.ObjectId;
import org.springframework.dao.DuplicateKeyException;
import pl.sages.javadevpro.projecttwo.domain.exception.DuplicateRecordException;
import pl.sages.javadevpro.projecttwo.domain.exception.RecordNotFoundException;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log
public class UserStorageAdapter implements UserRepository {

    private final MongoUserRepository userRepository;
    private final UserEntityMapper mapper;


    @Override
    public Optional<User> save(User user) {
        try {
            UserEntity saved = userRepository.insert(mapper.toEntity(user));
            log.info("Saved entity " + saved);
            return Optional.of(mapper.toDomain(saved));
        } catch (DuplicateKeyException ex) {
            log.warning("User " +  user.getEmail() + " already exits in db");
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> update(User user) {
        Optional<UserEntity> entity = userRepository.findById(user.getId());
        if (entity.isEmpty()) {
            return Optional.empty();
        }
        UserEntity updated = userRepository.save(mapper.toEntity(user));
        log.info("Updating task "+ updated);
        return Optional.of(mapper.toDomain(updated));
    }

    @Override
    public Optional<User> remove(String id) {
        Optional<UserEntity> entity = userRepository.findById(id);
        if(entity.isEmpty()) {
            return Optional.empty();
        }
        userRepository.deleteById(id);
        log.info("Removing user " + entity);
        return Optional.of(mapper.toDomain(entity.get()));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id).map(mapper::toDomain);
    }

    public Optional<User> findByName(String email) {
        Optional<UserEntity> entity = userRepository.findByName(email);
        if (entity.isEmpty()) {
            throw new RecordNotFoundException("User not found");
        }
        log.info("Found entity " + entity.map(Object::toString).orElse("none"));
        if (entity.isPresent()) {
            return entity
                .map(mapper::toDomain);
        }

        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

}