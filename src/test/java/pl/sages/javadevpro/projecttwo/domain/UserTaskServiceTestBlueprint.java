package pl.sages.javadevpro.projecttwo.domain;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
//import pl.sages.javadevpro.projecttwo.domain.usertask.DirectoryService;
//import pl.sages.javadevpro.projecttwo.domain.usertask.GitService;
//import pl.sages.javadevpro.projecttwo.domain.usertask.UserTaskService;


@ExtendWith(MockitoExtension.class)
class UserTaskServiceTestBlueprint {

//    private final static String TEST_DIRECTORY = "src/test/usertasktest";
//
//    @Mock private GitService gitService;
//    @Mock private DirectoryService directoryService;
//    @Mock private UserService userService;
//    @Mock private TaskBlueprintService taskBlueprintService;
//
//    @InjectMocks
//    private UserTaskService userTaskService;
//
//    private final User fakeUser = new User(
//            "ID28",
//            "email@email.any",
//            "user name",
//            "pass",
//            List.of(UserRole.STUDENT)
//    );
//    private final TaskBlueprint fakeTaskBlueprint = new TaskBlueprint(
//            "1",
//            "task name",
//            "description",
//            "/path/sample");
//
//    @BeforeEach
//    void prepareMocks() {
//        when(directoryService.createDirectoryForUserTask(Mockito.any(),Mockito.anyString())).thenReturn(TEST_DIRECTORY);
//        when(userService.findById(Mockito.anyString())).thenReturn(fakeUser);
//        when(taskBlueprintService.findBy(Mockito.anyString())).thenReturn(fakeTaskBlueprint);
//    }
//
//
//    @DisplayName("Should create new UserTask")
//    @Test
//    void shouldCreateNewUserTask() {
//        String userEmail = fakeUser.getEmail();
//        String taskId = fakeTaskBlueprint.getId();
//
//        //when
//        Task task = userTaskService.assignTask(userEmail, taskId);
//
//        //then
//        Assertions.assertNotNull(task);
//        Assertions.assertEquals(fakeTaskBlueprint.getId(), task.getId());
//        Assertions.assertEquals(fakeTaskBlueprint.getName(), task.getName());
//        Assertions.assertEquals(fakeTaskBlueprint.getDescription(), task.getDescription());
////        Assertions.assertEquals(fakeUser.getId(), task.getUserId());
////        Assertions.assertEquals(TEST_DIRECTORY, task.getUserTaskFolder());
////        Assertions.assertEquals(TaskStatus.NOT_STARTED, task.getTaskStatus());
//    }

}
