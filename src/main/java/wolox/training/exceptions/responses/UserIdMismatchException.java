package wolox.training.exceptions.responses;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserIdMismatchException extends RuntimeException{
    public UserIdMismatchException(String msg) {
        super(msg);
    }
}
