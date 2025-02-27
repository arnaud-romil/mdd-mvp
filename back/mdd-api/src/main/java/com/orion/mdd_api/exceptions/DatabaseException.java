package com.orion.mdd_api.exceptions;

/** Exception thrown when a database error occurs. */
public class DatabaseException extends RuntimeException {

  public DatabaseException(Throwable cause) {
    super("Database error occurred", cause);
  }
}
