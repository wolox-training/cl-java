package wolox.training.exceptions.responses;

public class BookAlreadyOwnException extends RuntimeException{

    public BookAlreadyOwnException(String msg) {
        super(msg);
    }
}
