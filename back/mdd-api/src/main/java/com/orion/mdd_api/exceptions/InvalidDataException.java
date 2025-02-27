package com.orion.mdd_api.exceptions;

/** Exception thrown when an invalid data is sent to the application */
public class InvalidDataException extends RuntimeException {

  public InvalidDataException(String message) {
    super(message);
  }

  public InvalidDataException(String message, Throwable cause) {
    super(message, cause);
  }
}
