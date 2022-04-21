package pl.sages.javadevpro.projecttwo.external.storage.assigment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Assignments")
@TypeAlias("AssigmentEntity")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssigmentEntity {

    private String id;
    private String userId;
    private String taskId;

    public AssigmentEntity(String userId, String taskId) {
        this.userId = userId;
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssigmentEntity that = (AssigmentEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}

