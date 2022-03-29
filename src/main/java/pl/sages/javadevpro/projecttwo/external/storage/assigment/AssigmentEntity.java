package pl.sages.javadevpro.projecttwo.external.storage.assigment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class AssigmentEntity {


    //todo do sprawdzenie dlaczego to nie jest entity
    //todo analogicznie do tego co jest UserEntity
    private String id;
    private String userId;
    private String taskId;

}

