package tableservice.exception;

public class NoAvailabilityException extends RuntimeException {
    public NoAvailabilityException(String message) {
        super(message);
    }
}