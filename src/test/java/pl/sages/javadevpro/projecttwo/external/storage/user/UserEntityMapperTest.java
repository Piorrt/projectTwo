package pl.sages.javadevpro.projecttwo.external.storage.user;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.sages.javadevpro.projecttwo.api.user.UserDto;
import pl.sages.javadevpro.projecttwo.api.user.UserDtoMapper;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.usertask.TaskStatus;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTask;
import pl.sages.javadevpro.projecttwo.external.storage.usertask.UserTaskEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class UserEntityMapperTest {

    private final UserEntityMapper userEntityMapper = new UserEntityMapper();

    private final static String USER_NAME = "User1";
    private final static String USER_EMAIL = "user1@email.com";
    private final static String USER_PASSWORD = "password";
    private final static List<String> USER_ROLES = List.of("STUDENT", "ADMIN");
    private final static UserTaskEntity USER_TASK_ENTITY = new UserTaskEntity("1", "Task 1", "Discription1", "/folder", "NOT_STARTED");
    private final static List<UserTaskEntity> TASKS_ENTITY = List.of(USER_TASK_ENTITY);
    private final static UserTask USER_TASK = new UserTask("2", "Task 2", "Discription2", "/folder", TaskStatus.NOT_STARTED, USER_EMAIL);
    private final static List<UserTask> TASKS_DOMAIN = List.of(USER_TASK);

    @Test
    void user_entity_values_should_be_mapped_directly_to_domain(){
        UserEntity entity = UserEntity.builder()
                .name(USER_NAME)
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .roles(USER_ROLES)
                .tasks(TASKS_ENTITY)
                .build();
        User domain = userEntityMapper.toDomain(entity);

        assertEquals(USER_NAME, domain.getName());
        assertEquals(USER_EMAIL, domain.getEmail());
        assertEquals(USER_PASSWORD, domain.getPassword());
        assertEquals(USER_ROLES, domain.getRoles());
        assertEquals(TaskStatus.NOT_STARTED, domain.getTasks().get(0).getTaskStatus());
    }


    @Test
    void user_domain_values_should_be_mapped_directly_to_entity(){

        User domain = new User();
        domain.setName(USER_NAME);
        domain.setPassword(USER_PASSWORD);
        domain.setEmail(USER_EMAIL);
        domain.setRoles(USER_ROLES);
        domain.setTasks(TASKS_DOMAIN);

        UserEntity entity = userEntityMapper.toEntity(domain);

        assertEquals(USER_NAME, entity.getName());
        assertEquals(USER_EMAIL, entity.getEmail());
        assertEquals(USER_PASSWORD, entity.getPassword());
        assertEquals(USER_ROLES, entity.getRoles());
        assertEquals("NOT_STARTED", entity.getTasks().get(0).getTaskStatus());
    }
}
