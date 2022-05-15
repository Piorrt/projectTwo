package pl.sages.javadevpro.projecttwo.external.storage.user;

import org.mapstruct.Mapper;
import pl.sages.javadevpro.projecttwo.domain.user.model.User;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    UserEntity toEntity(User domain);

    User toDomain(UserEntity entity);

}
