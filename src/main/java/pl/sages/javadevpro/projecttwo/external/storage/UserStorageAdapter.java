package pl.sages.javadevpro.projecttwo.external.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.dao.DuplicateKeyException;
import pl.sages.javadevpro.projecttwo.domain.exception.DuplicateRecordException;
import pl.sages.javadevpro.projecttwo.domain.exception.RecordNotFoundException;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.user.UserRepository;
import pl.sages.javadevpro.projecttwo.external.storage.user.MongoUserRepository;
import pl.sages.javadevpro.projecttwo.external.storage.user.UserEntity;
import pl.sages.javadevpro.projecttwo.external.storage.user.UserEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Optional<UserEntity> entity = userRepository.findById(user.getId());
        if (entity.isEmpty()) {
            throw new RecordNotFoundException("Task not found");
        }
        UserEntity updated = userRepository.save(mapper.toEntity(user));
        log.info("Updating task "+ updated);
        return mapper.toDomain(updated);
    }

    @Override
    public void remove(User user) {
        Optional<UserEntity> entity = userRepository.findById(user.getId());
        if(entity.isEmpty()) {
            throw new RecordNotFoundException("User not exist!");
        }
        UserEntity userEntity = mapper.toEntity(user);
        log.info("Removing user " + userEntity.toString());
        userRepository.delete(userEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> entity = userRepository.findByEmail(email);
        if (entity.isPresent()) {
            log.info("Found entity " + entity.map(Object::toString).orElse("none"));
            return entity.map(mapper::toDomain);
        } else {
            throw new RecordNotFoundException("User not found");
        }
    }

    @Override
    public Optional<User> findById(String id) {
        Optional<UserEntity> entity = userRepository.findById(id);
        if (entity.isPresent()) {
            log.info("Found entity " + entity.map(Object::toString).orElse("none"));
            return entity.map(mapper::toDomain);
        } else {
            throw new RecordNotFoundException("User not found");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
}