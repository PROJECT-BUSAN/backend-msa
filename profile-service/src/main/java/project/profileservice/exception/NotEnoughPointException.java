package project.profileservice.exception;

public class NotEnoughPointException extends RuntimeException{
    public NotEnoughPointException() {
        super();
    }

    public NotEnoughPointException(String message) {
        super(message);
    }
}
