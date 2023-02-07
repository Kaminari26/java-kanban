package manager;

public class TaskValidationException extends RuntimeException {
    public TaskValidationException(String message) {
        super(message);
    }
    public TaskValidationException(String message, Throwable cause) {
        super(message, cause);
    }
    public TaskValidationException(Throwable cause) {
        super(cause);
    }
}
