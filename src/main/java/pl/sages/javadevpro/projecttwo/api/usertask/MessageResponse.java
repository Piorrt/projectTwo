package pl.sages.javadevpro.projecttwo.api.usertask;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageResponse {

    //todo 8. zrobić ErrorResponse do użycia w CustomExceptionHandler -- status do usunięcia
    private String status;
    private String message;
}
