package wolox.training.exceptions.responses;

public class UsernameAlreadyTakenException extends RuntimeException{

    public UsernameAlreadyTakenException(String msg) {
        super(msg);
    }
}
