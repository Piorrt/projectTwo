package pl.sages.javadevpro.projecttwo.domain.usertask;

import pl.sages.javadevpro.projecttwo.domain.task.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LocalDirectoryHandler implements DirectoryHandler {

    @Override
    public String createDirectoryForUserTask(Task task, String userEmail) {
        String convertedEmail = removeSymbolsFromEmail(userEmail);
        String path = "userTasks/" + convertedEmail + "/" + task.getId();

        Path folderPath = Path.of(path);
        if (!Files.isDirectory(folderPath)) {
            try {
                Files.createDirectories(folderPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    private String removeSymbolsFromEmail(String email) {
        return email.replace("@","").replace(".","");
    }
}
