package Exception;


public class CustomCheckedException extends Exception {
    private final String detailMessage;

    public CustomCheckedException(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    @Override
    public String getMessage() {
        return "Проверяемое исключение: " + detailMessage;
    }
}