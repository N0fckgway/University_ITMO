package Exception;


public class CustomSetDeathException extends Exception {
    private final String detailMessage;

    public CustomSetDeathException(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    @Override
    public String getMessage() {
        return "Проверяемое исключение: " + detailMessage;
    }
}