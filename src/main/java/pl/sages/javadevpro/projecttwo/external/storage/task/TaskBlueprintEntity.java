package pl.sages.javadevpro.projecttwo.external.storage.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
@Document("TaskBlueprints")
@TypeAlias("TaskBlueprintEntity")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskBlueprintEntity {

    @Id
    private String id;
    private String name;
    private String description;
    private String repositoryUrl;

    public TaskBlueprintEntity(String name, String description, String repositoryUrl) {
        this.name = name;
        this.description = description;
        this.repositoryUrl = repositoryUrl;
    }

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
