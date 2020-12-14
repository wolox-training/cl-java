package wolox.training.exceptions.responses;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String msg) {
        super(msg);
    }
}
