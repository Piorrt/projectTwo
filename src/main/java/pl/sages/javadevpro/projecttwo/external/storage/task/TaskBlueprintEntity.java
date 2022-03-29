package pl.sages.javadevpro.projecttwo.external.storage.task;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@TypeAlias("TaskBlueprints")//todo analogicznie do tego co jest UserEntity
@Document(value = "TaskBlueprintEntity")
public class TaskBlueprintEntity {

    @Id
    private String id;
    private String name;
    private String description;
    private String repositoryUrl;

}
