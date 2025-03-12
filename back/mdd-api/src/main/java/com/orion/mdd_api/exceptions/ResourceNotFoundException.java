package com.orion.mdd_api.exceptions;

/** Exception thrown when a resource is not found in the database */
public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message) {
    super(message);
  }
}
