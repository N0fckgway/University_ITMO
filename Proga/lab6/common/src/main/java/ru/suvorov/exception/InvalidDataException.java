package ru.suvorov.exception;


public class InvalidDataException extends RuntimeException {
    private final Object obj;


    public InvalidDataException(Object obj, String message) {
        super(message);
        this.obj = obj;
    }

    @Override
    public String getMessage() {
        return "Ошибка в объекте " + obj.getClass().getSimpleName() + ": " + super.getMessage();
    }
}
