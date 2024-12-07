package itmo.is.exception;

public class UniqueConstraintViolationException extends IllegalArgumentException {
    public UniqueConstraintViolationException(String message) {
        super(message);
    }
}
