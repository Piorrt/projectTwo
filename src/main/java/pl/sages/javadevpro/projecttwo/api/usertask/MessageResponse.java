package pl.sages.javadevpro.projecttwo.api.usertask;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageResponse {

    //todo zrobić ErrorResponse do użycia w CustomExceptionHandler -- status do usunięcia
    private String status;
    private String message;
}
