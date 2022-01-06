package pl.sages.javadevpro.projecttwo.external.git;

import lombok.SneakyThrows;
import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Component;
import pl.sages.javadevpro.projecttwo.domain.usertask.GitService;

import java.io.File;

@Component
public class JGitService implements GitService {

    @SneakyThrows
    @Override
    public File cloneTask(String repositoryPath, String destinationPath) {
        File repo = new File(destinationPath);
        Git git = Git.cloneRepository()
                .setURI(repositoryPath)
                .setDirectory(repo)
                .call();
        git.close();
        return git.getRepository().getDirectory();
    }

}
