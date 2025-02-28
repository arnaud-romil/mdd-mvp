package com.orion.mdd_api.exceptions;

import com.orion.mdd_api.payloads.responses.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidDataException.class)
  public ResponseEntity<MessageResponse> handleInvalidDataException(InvalidDataException ex) {
    return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
  }

  @ExceptionHandler(UserUnauthorizedException.class)
  public ResponseEntity<MessageResponse> handleUserUnauthorizedException(
      UserUnauthorizedException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new MessageResponse(ex.getMessage()));
  }

  @ExceptionHandler(DatabaseException.class)
  public ResponseEntity<String> handleDatabaseException(DatabaseException ex) {
    return ResponseEntity.internalServerError().body(ex.getMessage());
  }

  @ExceptionHandler(UserForbiddenException.class)
  public ResponseEntity<MessageResponse> handleInvalidDataException(UserForbiddenException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(ex.getMessage()));
  }
}
