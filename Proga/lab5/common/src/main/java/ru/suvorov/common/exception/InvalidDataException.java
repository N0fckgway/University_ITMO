package ru.suvorov.common.exception;


public class InvalidDataException extends RuntimeException {
    private final Object obj;


    public InvalidDataException(Object obj, String message) {
        super(message);
        this.obj = obj;
    }

    @Override
    public String getMessage() {
        return obj.getClass() + ":" + super.getMessage();
    }
}
