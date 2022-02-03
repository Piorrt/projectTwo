package pl.sages.javadevpro.projecttwo.external.directory;

import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.domain.usertask.DirectoryService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalDirectoryService implements DirectoryService {

    @Override
    public String createDirectoryForUserTask(Task task, String userEmail) {
        String convertedEmail = removeSymbolsFromEmail(userEmail);
        String path = "userTasks/" + convertedEmail + "/" + task.getId();

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

    private String removeSymbolsFromEmail(String email) {
        return email.replace("@","").replace(".","");
    }
}
