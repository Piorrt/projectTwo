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

    //todo do sprawdzenie dlaczego to nie jest entity
    //todo analogicznie do tego co jest UserEntity//todo analogicznie do tego co jest UserEntity
    @Id
    String id;
    String name;
    String description;
    String workspaceUrl;
    TaskStatus status;

}
