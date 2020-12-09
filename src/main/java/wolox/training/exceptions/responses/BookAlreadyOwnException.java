package wolox.training.exceptions.responses;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BookAlreadyOwnException extends RuntimeException{

    public BookAlreadyOwnException(String msg) {
        super(msg);
    }
}
