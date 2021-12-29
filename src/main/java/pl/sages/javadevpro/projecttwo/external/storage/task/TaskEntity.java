package pl.sages.javadevpro.projecttwo.external.storage.task;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document
public class TaskEntity {

    @Id
    private String id;
    private String name;
    private String description;

}