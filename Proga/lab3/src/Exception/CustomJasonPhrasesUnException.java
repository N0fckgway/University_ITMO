package Exception;


public class CustomJasonPhrasesUnException extends RuntimeException {
    private final String detailMessage;

    public CustomJasonPhrasesUnException(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    @Override
    public String getMessage() {
        return "Непроверяемое исключение: " + detailMessage;
    }
}
