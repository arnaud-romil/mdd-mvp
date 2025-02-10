package com.orion.mdd_api.exceptions;

public class DatabaseException extends RuntimeException {

  public DatabaseException(Throwable cause) {
    super("Database error occurred", cause);
  }
}
