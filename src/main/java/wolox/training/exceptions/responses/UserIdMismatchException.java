package wolox.training.exceptions.responses;

public class UserIdMismatchException extends RuntimeException{
    public UserIdMismatchException(String msg) {
        super(msg);
    }
}
