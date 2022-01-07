package pl.sages.javadevpro.projecttwo.domain.usertask;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sages.javadevpro.projecttwo.domain.task.Task;

import java.io.File;

@ExtendWith(MockitoExtension.class)
class LocalDirectoryServiceTest {

    private final LocalDirectoryService localDirectoryHandler = new LocalDirectoryService();
    private final static String TEST_DIRECTORY = "userTasks/sampleemailcom/1";
    private final static String USER_EMAIL = "sample@email.com";

    @Mock
    private Task task;

    @BeforeEach
    void defineMocks() {
        Mockito.when(task.getId()).thenReturn("1");
    }

    @DisplayName("should create new directory basing on user email and task id")
    @Test
    void shouldCreateNewDirectory() {
        String directoryPath = localDirectoryHandler.createDirectoryForUserTask(task,USER_EMAIL);

        File directory = new File(directoryPath);

        Assertions.assertEquals(TEST_DIRECTORY, directoryPath);
        Assertions.assertTrue(directory.exists());
        Assertions.assertTrue(directory.isDirectory());

    }


}
