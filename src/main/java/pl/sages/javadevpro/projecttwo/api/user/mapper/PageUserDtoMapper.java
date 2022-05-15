package pl.sages.javadevpro.projecttwo.api.user.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.sages.javadevpro.projecttwo.api.user.dto.PageUserDto;
import pl.sages.javadevpro.projecttwo.api.user.dto.UserDto;
import pl.sages.javadevpro.projecttwo.domain.user.model.PageUser;
import pl.sages.javadevpro.projecttwo.domain.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PageUserDtoMapper {

    @Mapping(target = "users", qualifiedByName = "toUserDtoList")
    PageUserDto toPageDto(PageUser domain);

    @Named("toUserDtoList")
    @IterableMapping(qualifiedByName = "userToUserDto")
    List<UserDto> toListDto(List<User> users);

    @Named("userToUserDto")
    @Mapping(target="password", constant = "######")
    UserDto toDto(User domain);
}