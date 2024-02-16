package com.fastcampus.board.exception;

import com.fastcampus.board.model.error.ClientErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ClientErrorException.class)
  public ResponseEntity<ClientErrorResponse> handleClientErrorException(ClientErrorException e) {
    return new ResponseEntity<>(
        new ClientErrorResponse(e.getStatus(), e.getMessage()), e.getStatus());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ClientErrorResponse> handleClientErrorException(
      HttpMessageNotReadableException e) {
    return new ResponseEntity<>(
        new ClientErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ClientErrorResponse> handleClientErrorException(
      MethodArgumentNotValidException e) {
    var errorMessage =
        e.getFieldErrors().stream()
            .map(fieldError -> (fieldError.getField() + ": " + fieldError.getDefaultMessage()))
            .toList()
            .toString();

    return new ResponseEntity<>(
        new ClientErrorResponse(HttpStatus.BAD_REQUEST, errorMessage), HttpStatus.BAD_REQUEST);
  }
}
