package orderservice.entity;
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}