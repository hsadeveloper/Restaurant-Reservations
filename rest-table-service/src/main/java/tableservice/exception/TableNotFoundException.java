package tableservice.exception;

public class TableNotFoundException extends RuntimeException {
    public TableNotFoundException(String message) {
        super(message);
    }
}