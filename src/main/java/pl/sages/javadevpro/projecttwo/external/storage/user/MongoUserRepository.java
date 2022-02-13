package pl.sages.javadevpro.projecttwo.external.storage.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MongoUserRepository extends MongoRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
}
