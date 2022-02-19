package pl.sages.javadevpro.projecttwo.external.directory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import pl.sages.javadevpro.projecttwo.domain.exception.ResourceNotFoundException;
import pl.sages.javadevpro.projecttwo.domain.task.TaskBlueprint;
import pl.sages.javadevpro.projecttwo.domain.usertask.DirectoryService;
import pl.sages.javadevpro.projecttwo.external.directory.task.FileToBeDeliveredToUser;
import pl.sages.javadevpro.projecttwo.external.directory.task.TaskDefinition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class LocalDirectoryService implements DirectoryService {

    private static final String SUMMARY_RESULT_FILE_PATH = "/test_results/test_summary.txt";
    private static final String TASK_DEFINITION_FILE_PATH = "/task_definition.yml";

    private final String baseLocalFolder;

    public LocalDirectoryService(String baseLocalFolder) {
        this.baseLocalFolder = baseLocalFolder;
    }

    @Override
    public String createDirectoryForUserTask(TaskBlueprint taskBlueprint, String userEmail) {
        String path = getPathToUserTask(userEmail, taskBlueprint.getId());

        Path folderPath = Path.of(path);
        Path absolutePath = folderPath.toAbsolutePath();
        if (!Files.isDirectory(folderPath)) {
            try {
                Files.createDirectories(folderPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return absolutePath.toString();
    }

    @Override
    public List<String> readListOfAvailableFilesForUserTask(String userEmail, String taskId) {
        String path = getPathToUserTask(userEmail, taskId) + TASK_DEFINITION_FILE_PATH;

        var mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();

        TaskDefinition taskDefinition;
        try {
            taskDefinition = mapper.readValue(new File(path), TaskDefinition.class);
        } catch (IOException e) {
            throw new ResourceNotFoundException("Task " + taskId + " was not assigned to user " + userEmail);
        }

        return taskDefinition
                .getFilesToBeDeliveredToUser()
                .stream()
                .map(FileToBeDeliveredToUser::getFile)
                .collect(Collectors.toList());
    }

    @Override
    public void uploadFileForUserTask(String userEmail, String taskId, String fileId, byte[] bytes) {
        File file = takeFileFromUserTask(userEmail, taskId, fileId);

        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public File takeFileFromUserTask(String userEmail, String taskId, String fileId) {
        List<String> listOfAvailableFilesForUserTask = readListOfAvailableFilesForUserTask(userEmail, taskId);

        if (Integer.parseInt(fileId) > listOfAvailableFilesForUserTask.size()) {
            throw new ResourceNotFoundException("File " + fileId + " was not assigned to " + taskId + " user task " + userEmail);
        }

        String relatedPathToSelectedFile = listOfAvailableFilesForUserTask.get(Integer.parseInt(fileId) - 1);

        String path = getPathToUserTask(userEmail, taskId) + "/" + relatedPathToSelectedFile;

        return new File(path);
    }

    @Override
    public String getPathToUserTask(String userEmail, String taskId) {
        String convertedEmail = userEmail.replace("@","").replace(".","");
        return  baseLocalFolder + convertedEmail + "/" + taskId;
    }

    @Override
    public File getResultFile(String userEmail, String taskId) {
        String path = getPathToUserTask(userEmail,taskId);
        return new File(path + SUMMARY_RESULT_FILE_PATH);
    }

}
