package dev.n0fckgway.lab2.exceptions;



public class InvalidDataException extends RuntimeException {
    public Object object;
    public InvalidDataException(Object object, String message) {
        super(message);
        this.object = object;

    }

    @Override
    public String getMessage() {
        return "В " + object + " " + getMessage();
    }
}
