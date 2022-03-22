package pl.sages.javadevpro.projecttwo.api.task.verification;

public class UserIsNotAuthorizedToThisTaskException extends RuntimeException{

    public UserIsNotAuthorizedToThisTaskException(String message) {
        super(message);
    }

}