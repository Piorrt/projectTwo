package pl.sages.javadevpro.projecttwo.external.storage.usertask;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UserTaskEntity {

    //todo do sprawdzenie dlaczego to nie jest entity
    //todo analogicznie do tego co jest UserEntity
    private String id;
    private String name;
    private String description;
    private String userTaskFolder;
    private String taskStatus;
}
