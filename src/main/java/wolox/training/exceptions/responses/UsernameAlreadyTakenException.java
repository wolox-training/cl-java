package wolox.training.exceptions.responses;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameAlreadyTakenException extends RuntimeException{

    public UsernameAlreadyTakenException(String msg) {
        super(msg);
    }
}
