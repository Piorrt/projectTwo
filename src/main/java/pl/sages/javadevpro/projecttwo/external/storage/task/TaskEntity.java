package pl.sages.javadevpro.projecttwo.external.storage.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import pl.sages.javadevpro.projecttwo.domain.task.TaskStatus;

@AllArgsConstructor
@Setter
@Getter
public class TaskEntity {

    @Id
    String id;
    String name;
    String description;
    String workspaceUrl;
    TaskStatus status;

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
