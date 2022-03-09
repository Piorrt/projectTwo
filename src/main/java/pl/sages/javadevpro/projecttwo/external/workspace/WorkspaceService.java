package pl.sages.javadevpro.projecttwo.external.workspace;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.springframework.stereotype.Service;
import pl.sages.javadevpro.projecttwo.domain.exception.DuplicateRecordException;
import pl.sages.javadevpro.projecttwo.domain.exception.ResourceNotFoundException;
import pl.sages.javadevpro.projecttwo.domain.task.Workspace;
import pl.sages.javadevpro.projecttwo.external.directory.task.FileToBeDeliveredToUser;
import pl.sages.javadevpro.projecttwo.external.directory.task.TaskDefinition;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkspaceService implements Workspace {

    private static final String SUMMARY_RESULT_FILE_PATH = "/test_results/test_summary.txt";
    private static final String TASK_DEFINITION_FILE_PATH = "/task_definition.yml";
    private static final String WORKSPACE_BASE_DIRECTORY_PATH = "/workspaces/";

    @Override
    @SneakyThrows(GitAPIException.class)
    public String createWorkspace(String sourceRepositoryUrl) {
        String destinationPath = WORKSPACE_BASE_DIRECTORY_PATH + UUID.randomUUID();
        File repo = new File(destinationPath);
        Git git;
        try {
            git = Git.cloneRepository()
                    .setURI(sourceRepositoryUrl)
                    .setDirectory(repo)
                    .call();
        } catch (JGitInternalException e) {
            throw new RepositoryAlreadyResidesInDestinationFolderException("Repository already resides in destination folder.");
        }
        unlinkRemotes(git);
        git.close();
        return destinationPath;
    }

    @Override
    public List<String> getFilesList(String rootPathUrl) {
        String path = rootPathUrl + TASK_DEFINITION_FILE_PATH;

        var mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();

        TaskDefinition taskDefinition;
        try {
            taskDefinition = mapper.readValue(new File(path), TaskDefinition.class);
        } catch (IOException e) {
            throw new TaskWasNotCreatedProperlyException("Task was not created properly");
        }

        return taskDefinition
                .getFilesToBeDeliveredToUser()
                .stream()
                .map(FileToBeDeliveredToUser::getFile)
                .collect(Collectors.toList());
    }

    @Override
    public void writeFile(String rootPathUrl, String path, byte[] bytes) {

    }

    @Override
    public byte[] readFile(String rootPathUrl, String path) {
        return new byte[0];
    }

    @Override
    public void commitChanges(String rootPathUrl) {

    }



    private void unlinkRemotes(Git git) {
        Set<String> remoteNames = git.getRepository().getRemoteNames();
        remoteNames.forEach(g -> {
            try {
                git.remoteRemove().setRemoteName(g).call();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        });
    }

}
