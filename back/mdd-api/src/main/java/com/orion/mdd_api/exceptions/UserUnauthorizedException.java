package com.orion.mdd_api.exceptions;

/** Exception thrown when the user is not authorized for an operation. */
public class UserUnauthorizedException extends RuntimeException {

  public UserUnauthorizedException(String message) {
    super(message);
  }
}
