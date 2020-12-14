package wolox.training.exceptions.responses;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String msg) {
        super(msg);
    }
}
