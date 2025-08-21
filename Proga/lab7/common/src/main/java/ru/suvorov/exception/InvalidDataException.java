package ru.suvorov.exception;



public class InvalidDataException extends RuntimeException {
  private final Object object;

  public InvalidDataException(Object obj, String message) {
    super(message);
    this.object = obj;
  }


  @Override
  public String getMessage() {
    return object.getClass() + ":" + super.getMessage();
  }
}
