package Exception;


public class CustomJasonPhrasesException extends Exception {
    private final String detailMessage;

    public CustomJasonPhrasesException(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    @Override
    public String getMessage() {
        return "Проверяемое исключение: " + detailMessage;
    }
}