package pl.sages.javadevpro.projecttwo.api.task.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskControllerCommandTest {

    @Test
    void has_name_should_return_true_when_command_has_given_name() {
        TaskControllerCommand taskControllerCommand = new TaskControllerCommand(CommandName.EXECUTE);
        assertEquals(true, taskControllerCommand.hasName(CommandName.EXECUTE));
    }

    @Test
    void has_name_should_return_false_when_command_has_not_given_name() {
        TaskControllerCommand taskControllerCommand = new TaskControllerCommand(CommandName.EXECUTE);
        assertEquals(false, taskControllerCommand.hasName(null));
    }
}