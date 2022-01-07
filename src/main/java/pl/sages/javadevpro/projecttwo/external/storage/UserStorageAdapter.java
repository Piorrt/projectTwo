package pl.sages.javadevpro.projecttwo.external.storage;

import org.springframework.dao.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import pl.sages.javadevpro.projecttwo.domain.exception.DuplicateRecordException;
import pl.sages.javadevpro.projecttwo.domain.exception.RecordNotFoundException;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.user.UserRepository;
import pl.sages.javadevpro.projecttwo.external.storage.user.MongoUserRepository;
import pl.sages.javadevpro.projecttwo.external.storage.user.UserEntity;
import pl.sages.javadevpro.projecttwo.external.storage.user.UserEntityMapper;

import java.util.Optional;

@RequiredArgsConstructor
@Log
public class UserStorageAdapter implements UserRepository {

    private final MongoUserRepository userRepository;
    private final UserEntityMapper mapper;


    @Override
    public User save(User user) {
        try {
            userRepository.insert(mapper.toEntity(user));
            UserEntity saved = mapper.toEntity(user);
            log.info("Saved entity " + saved.toString());
            return mapper.toDomain(saved);
        } catch (DuplicateKeyException ex) {
            log.warning("User " +  user.getEmail() + " already exits in db");
            throw new DuplicateRecordException("User already exits");
        }
    }

    @Override
    public User update(User user) {
        Optional<UserEntity> entity = userRepository.findById(user.getEmail());
        if (entity.isEmpty()) {
            throw new RecordNotFoundException("Task not found");
        }
        UserEntity updated = userRepository.save(mapper.toEntity(user));
        log.info("Updating task "+ updated);
        return mapper.toDomain(updated);
    }

    @Override
    public void remove(User user) {
        Optional<UserEntity> entity = userRepository.findById(user.getEmail());
        if(entity.isEmpty()) {
            throw new RecordNotFoundException("User not exist!");
        }
        UserEntity userEntity = mapper.toEntity(user);
        log.info("Removing user " + userEntity.toString());
        userRepository.delete(userEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> entity = userRepository.findById(email);
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

}
