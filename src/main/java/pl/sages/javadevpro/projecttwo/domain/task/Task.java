package pl.sages.javadevpro.projecttwo.domain.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Task {
    private String id;
    private String name;
    private String description;
    private String repositoryPath;


    public Task updateTask(Task task){
        return new Task(
                this.id,
                task.name,
                task.description,
                task.repositoryPath
                );
    }
}
