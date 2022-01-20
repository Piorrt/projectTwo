package pl.sages.javadevpro.projecttwo.config;


import org.mapstruct.factory.Mappers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.sages.javadevpro.projecttwo.api.task.TaskDtoMapper;
import pl.sages.javadevpro.projecttwo.api.user.UserDtoMapper;
import pl.sages.javadevpro.projecttwo.api.usertask.UserTaskDtoMapper;
import pl.sages.javadevpro.projecttwo.domain.TaskService;
import pl.sages.javadevpro.projecttwo.domain.UserService;
import pl.sages.javadevpro.projecttwo.domain.UserTaskService;
import pl.sages.javadevpro.projecttwo.domain.task.TaskRepository;
import pl.sages.javadevpro.projecttwo.domain.user.UserRepository;
import pl.sages.javadevpro.projecttwo.domain.usertask.UserTaskExecutor;
import pl.sages.javadevpro.projecttwo.external.env.UserTaskEnvAdapter;
import pl.sages.javadevpro.projecttwo.external.env.kafka.KafkaUserTaskEnv;
import pl.sages.javadevpro.projecttwo.external.env.usertask.UserTaskEnvMapper;
import pl.sages.javadevpro.projecttwo.domain.usertask.DirectoryService;
import pl.sages.javadevpro.projecttwo.domain.usertask.GitService;
import pl.sages.javadevpro.projecttwo.external.directory.LocalDirectoryService;
import pl.sages.javadevpro.projecttwo.external.git.JGitService;
import pl.sages.javadevpro.projecttwo.external.storage.task.MongoTaskRepository;
import pl.sages.javadevpro.projecttwo.external.storage.task.TaskEntityMapper;
import pl.sages.javadevpro.projecttwo.external.storage.TaskStorageAdapter;
import pl.sages.javadevpro.projecttwo.external.storage.UserStorageAdapter;
import pl.sages.javadevpro.projecttwo.external.storage.user.MongoUserRepository;
import pl.sages.javadevpro.projecttwo.external.storage.user.UserEntityMapper;
import pl.sages.javadevpro.projecttwo.external.storage.usertask.UserTaskEntityMapper;

@Configuration
@ConfigurationProperties("domain.properties")
public class DomainConfiguration {


    @Bean
    public UserRepository userRepository(MongoUserRepository mongoUserRepository, UserEntityMapper mapper) {
        return new UserStorageAdapter(mongoUserRepository, mapper);
    }

    @Bean
    public UserService userService(UserRepository userRepository)  {
        return new UserService(userRepository);
    }

    @Bean
    public TaskRepository taskRepository( MongoTaskRepository taskRepository, TaskEntityMapper mapper ) {
        return new TaskStorageAdapter( taskRepository, mapper );
    }

    @Bean
    public TaskService taskService( TaskRepository taskRepository) {

        return new TaskService(taskRepository);
    }

    @Bean
    public UserDtoMapper userDtoMapper() {
        return Mappers.getMapper(UserDtoMapper.class);
    }

    @Bean
    public UserEntityMapper userEntityMapper() {
        return new UserEntityMapper();
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

    @Bean
    public UserTaskEntityMapper userTaskEntityMapper() {
        return Mappers.getMapper(UserTaskEntityMapper.class);
    }

    @Bean
    public UserTaskService userTaskService(
            GitService gitService,
            DirectoryService directoryService,
            UserService userService,
            TaskService taskService,
            UserTaskExecutor userTaskExecutor
    ) {
        return new UserTaskService(
                gitService,
                directoryService,
                userService,
                taskService,
                userTaskExecutor);
    }

    @Bean
    public UserTaskExecutor userTaskExecutor(KafkaUserTaskEnv userTaskExecutor, UserTaskEnvMapper userTaskExecMapper){
        return new UserTaskEnvAdapter(userTaskExecutor, userTaskExecMapper);}

    @Bean
    UserTaskEnvMapper userTaskEnvMapper() { return Mappers.getMapper(UserTaskEnvMapper.class); }

    @Bean
    public GitService gitService() {
        return new JGitService();
    }

    @Bean
    public DirectoryService directoryService(){
        return new LocalDirectoryService();
    }

}
