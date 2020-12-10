package wolox.training.exceptions.responses;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookAuthorAlreadyUsedException extends RuntimeException{
    public BookAuthorAlreadyUsedException(String msg) {
        super(msg);
    }
}
