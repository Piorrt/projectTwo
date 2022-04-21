package pl.sages.javadevpro.projecttwo.external.storage.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.sages.javadevpro.projecttwo.domain.task.TaskStatus;


@Document("Tasks")
@TypeAlias("TaskEntity")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskEntity {

    @Id
    String id;
    String name;
    String description;
    String workspaceUrl;
    TaskStatus status;

    public TaskEntity(String name, String description, String workspaceUrl, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.workspaceUrl = workspaceUrl;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity that = (TaskEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
