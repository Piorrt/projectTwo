package pl.sages.javadevpro.projecttwo.external.git;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class JGitServiceTest {

//    private final JGitService jGitService = new JGitService();
//
//    private final static String TEST_FOLDER = "testRepo/testProject";
//    private final static String TEST_REPO = "https://github.com/Piorrt/projectOne";
//
//    @BeforeEach
//    private void createTempFolder() throws IOException {
//        Path folderPath = Path.of(TEST_FOLDER);
//        if (!Files.isDirectory(folderPath)) {
//            Files.createDirectories(folderPath);
//        }
//    }
//
//    @DisplayName("should clone repository to specified folder")
//    @Test
//    void shouldCloneRepositoryToTargetFolder() throws IOException {
//        jGitService.cloneTask(TEST_REPO, TEST_FOLDER);
//
//        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
//        repositoryBuilder.setMustExist(true);
//        repositoryBuilder.setGitDir(new File(TEST_FOLDER + "/.git"));
//
//        Repository check = repositoryBuilder.build();
//        Assertions.assertDoesNotThrow(repositoryBuilder::build);
//        check.close();
//    }
//
//    @DisplayName("should unlink remotes after cloning repository")
//    @Test
//    void shouldUnlinkRemoteAfterCloning() throws IOException {
//        jGitService.cloneTask(TEST_REPO, TEST_FOLDER);
//
//        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
//        repositoryBuilder.setMustExist(true);
//        repositoryBuilder.setGitDir(new File(TEST_FOLDER + "/.git"));
//
//        Repository check = repositoryBuilder.build();
//        Assertions.assertTrue(check.getRemoteNames().isEmpty());
//        check.close();
//    }
//
//    @DisplayName("should throw exception if repository path is incorrect")
//    @Test
//    void shouldThrowExceptionIfRepositoryPathIsIncorrect() {
//        Assertions.assertThrows(
//                InvalidRemoteException.class,
//                () -> jGitService.cloneTask("false repo",TEST_FOLDER));
//    }
//
//    @DisplayName("should throw exception if target folder path is not empty")
//    @Test
//    void shouldThrowExceptionIfTargetPathIsNotEmpty() {
//        Assertions.assertThrows(
//                DuplicateRecordException.class,
//                () -> {
//                    jGitService.cloneTask(TEST_REPO,TEST_FOLDER);
//                    jGitService.cloneTask(TEST_REPO,TEST_FOLDER);
//                });
//    }
//
//
//    @AfterEach
//    private void tearDown() throws IOException {
//        Path folderPath = Path.of(TEST_FOLDER);
//        if (Files.exists(folderPath)) {
//            FileUtils.forceDelete(new File(TEST_FOLDER));
//        }
//    }



}
