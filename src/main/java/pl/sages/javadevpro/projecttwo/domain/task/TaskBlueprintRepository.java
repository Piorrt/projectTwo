package pl.sages.javadevpro.projecttwo.domain.task;

import java.util.Optional;

public interface TaskBlueprintRepository {

    TaskBlueprint save(TaskBlueprint taskBlueprint);

    Optional<TaskBlueprint> findById(String id);

    void remove(TaskBlueprint taskBlueprint);

    void update(TaskBlueprint taskBlueprint);

}
