package pl.sages.javadevpro.projecttwo.external.storage.user;

import org.apache.kafka.common.utils.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.user.UserRole;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserEntityMapperTest {

    private final UserEntityMapper userEntityMapper = Mappers.getMapper(UserEntityMapper.class);

    private final static String USER_ID = "ID30";
    private final static String USER_NAME = "User1";
    private final static String USER_EMAIL = "user1@email.com";
    private final static String USER_PASSWORD = "password";
    private final static Set<UserRole> USER_ROLES = Set.of(UserRole.STUDENT, UserRole.ADMIN);

    @Test
    void user_entity_values_should_be_mapped_directly_to_domain() {
        UserEntity entity = UserEntity.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .roles(USER_ROLES.stream().map(Enum::name).collect(Collectors.toSet()))
                .build();
        User domain = userEntityMapper.toDomain(entity);

        assertEquals(USER_ID, domain.getId());
        assertEquals(USER_NAME, domain.getName());
        assertEquals(USER_EMAIL, domain.getEmail());
        assertEquals(USER_PASSWORD, domain.getPassword());
        assertEquals(USER_ROLES, domain.getRoles());

    }

    @Test
    void user_domain_values_should_be_mapped_directly_to_entity() {

        User domain = new User(USER_ID, USER_EMAIL, USER_NAME, USER_PASSWORD, USER_ROLES);

        UserEntity entity = userEntityMapper.toEntity(domain);

        assertEquals(USER_ID, entity.getId());
        assertEquals(USER_NAME, entity.getName());
        assertEquals(USER_EMAIL, entity.getEmail());
        assertEquals(USER_PASSWORD, entity.getPassword());
        assertEquals(USER_ROLES.stream().map(Enum::name).collect(Collectors.toSet()), entity.getRoles());

    }
}
