package pl.sages.javadevpro.projecttwo.api.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.sages.javadevpro.projecttwo.api.task.verification.UserIsNotAuthorizedToThisTaskException;
import pl.sages.javadevpro.projecttwo.api.usertask.ErrorResponse;
import pl.sages.javadevpro.projecttwo.domain.task.TaskBlueprintNotFoundException;
import pl.sages.javadevpro.projecttwo.domain.user.UserNotFoundException;
import pl.sages.javadevpro.projecttwo.external.storage.task.TaskBlueprintAlreadyExistsException;
import pl.sages.javadevpro.projecttwo.external.storage.user.UserAlreadyExistsException;
import pl.sages.javadevpro.projecttwo.external.workspace.RepositoryWasNotFoundException;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TaskBlueprintNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleTaskBlueprintNotFoundException(TaskBlueprintNotFoundException ex) {
        return buildResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskBlueprintAlreadyExistsException.class)
    public final ResponseEntity<ErrorResponse> handleTaskBlueprintAlreadyExistsException(TaskBlueprintAlreadyExistsException ex) {
        return buildResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return buildResponse(ex,  HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public final ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return buildResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserIsNotAuthorizedToThisTaskException.class)
    public final ResponseEntity<ErrorResponse> handleUserIsNotAuthorizedToThisTaskException(UserIsNotAuthorizedToThisTaskException ex) {
        return buildResponse(ex, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(RepositoryWasNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleRepositoryWasNotFoundException(RepositoryWasNotFoundException ex) {
        return buildResponse(ex, HttpStatus.METHOD_NOT_ALLOWED);
    }

    private <E extends RuntimeException> ResponseEntity<ErrorResponse> buildResponse(E exception, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(exception.getMessage()));
    }
}