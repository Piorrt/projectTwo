package pl.sages.javadevpro.projecttwo.config;


import org.mapstruct.factory.Mappers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.sages.javadevpro.projecttwo.api.task.TaskDtoMapper;
import pl.sages.javadevpro.projecttwo.api.user.UserDtoMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import pl.sages.javadevpro.projecttwo.api.usertask.UserTaskDtoMapper;
import pl.sages.javadevpro.projecttwo.domain.TaskService;
import pl.sages.javadevpro.projecttwo.domain.UserService;
import pl.sages.javadevpro.projecttwo.domain.task.TaskRepository;
import pl.sages.javadevpro.projecttwo.domain.user.UserRepository;
import pl.sages.javadevpro.projecttwo.external.storage.task.MongoTaskRepository;
import pl.sages.javadevpro.projecttwo.external.storage.task.TaskEntityMapper;
import pl.sages.javadevpro.projecttwo.external.storage.TaskStorageAdapter;
import pl.sages.javadevpro.projecttwo.external.storage.UserStorageAdapter;
import pl.sages.javadevpro.projecttwo.external.storage.user.MongoUserRepository;
import pl.sages.javadevpro.projecttwo.external.storage.user.UserEntityMapper;

@Configuration
@ConfigurationProperties("domain.properties")
public class DomainConfiguration {


    @Bean
    public UserRepository userRepository(
            MongoUserRepository mongoUserRepository,
            UserEntityMapper mapper
    ) {
        return new UserStorageAdapter(
                mongoUserRepository,
                mapper
        );
    }


    @Bean
    public UserService userService(UserRepository userRepository)  {
        return new UserService(userRepository);
    }

    @Bean
    public TaskRepository taskRepository(MongoTaskRepository taskRepository, TaskEntityMapper mapper) {
        return new TaskStorageAdapter(taskRepository, mapper);
    }

    @Bean
    public TaskService taskService(TaskRepository taskRepository)  {
        return new TaskService(taskRepository);
    }

    @Bean
    public UserDtoMapper userDtoMapper() {
        return Mappers.getMapper(UserDtoMapper.class);
    }

    @Bean
    public UserEntityMapper userEntityMapper() {
        return Mappers.getMapper(UserEntityMapper.class);
    }

    @Bean
    public TaskDtoMapper taskDtoMapper() {
        return Mappers.getMapper(TaskDtoMapper.class);
    }

    @Bean
    public TaskEntityMapper taskEntityMapper() {
        return Mappers.getMapper(TaskEntityMapper.class);
    }

    @Bean
    public UserTaskDtoMapper userTaskDtoMapper() {
        return Mappers.getMapper(UserTaskDtoMapper.class);
    }
}
