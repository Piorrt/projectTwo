package pl.sages.javadevpro.projecttwo.external.storage.task;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@TypeAlias("TaskBlueprints")
@Document(value = "TaskBlueprintEntity")
public class TaskBlueprintEntity {

    @Id
    private String id;
    private String name;
    private String description;
    private String repositoryUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskBlueprintEntity that = (TaskBlueprintEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
