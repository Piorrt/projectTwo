package pl.sages.javadevpro.projecttwo.api.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.sages.javadevpro.projecttwo.domain.user.User;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    @Mapping(target="password", constant = "######")
    UserDto toDto(User domain);

    User toDomain(UserDto dto);

}
