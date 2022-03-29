package pl.sages.javadevpro.projecttwo.external.directory.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskDefinition {
    //todo Set zamiast listy
    private List<FileToBeDeliveredToUser> filesToBeDeliveredToUser;
    private Environment environment;
    private String taskVerificationMethod;
    //todo Set zamiast listy
    private List<ResultFile> resultFiles;
}
