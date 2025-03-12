package com.orion.mdd_api.exceptions;

/** Exception thrown when the operation is forbidden for the user */
public class UserForbiddenException extends RuntimeException {

  public UserForbiddenException(String message) {
    super(message);
  }
}
