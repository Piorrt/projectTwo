package pl.sages.javadevpro.projecttwo.api.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskControllerCommand {
    CommandName commandName;

    //todo 1. dodać metodę hasName
}
