package pl.sages.javadevpro.projecttwo.api.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.sages.javadevpro.projecttwo.domain.exception.DuplicateRecordException;
import pl.sages.javadevpro.projecttwo.domain.exception.RecordNotFoundException;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RecordNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException() {
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public final ResponseEntity handleDuplicateRecordException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}