package pl.sages.javadevpro.projecttwo.domain.usertask;

import java.io.File;

public interface GitService {

    File cloneTask(String repositoryPath, String destinationPath);

}
