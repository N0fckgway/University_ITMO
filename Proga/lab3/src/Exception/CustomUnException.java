package Exception;

// Непроверяемое исключение
public class CustomUnException extends Exception {
    private final String detailMessage;

    public CustomUnException(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    @Override
    public String getMessage() {
        return "Непроверяемое исключение: " + detailMessage;
    }
}
