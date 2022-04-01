package pl.sages.javadevpro.projecttwo.external.directory.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskDefinition {

    private Set<FileToBeDeliveredToUser> filesToBeDeliveredToUser;
    private Environment environment;
    private String taskVerificationMethod;
    private Set<ResultFile> resultFiles;
}