package com.example.blogapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.blogapp.dtos.ApiErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@ControllerAdvice
@Slf4j
public class ErrorController {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {
    log.error("caught exception", ex);
    ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .errorMsg("unexpected error occured")
        .build();
    return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiErrorResponse> handleIllegalArgumentError(IllegalArgumentException ex) {
    log.error("illegal argument exception", ex);
    ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder().status(HttpStatus.BAD_REQUEST.value())
        .errorMsg(ex.getMessage())
        .build();
    return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ApiErrorResponse> handleIllegalStateError(IllegalStateException ex) {
    log.error("illegal argument exception", ex);
    ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder().status(HttpStatus.CONFLICT.value())
        .errorMsg(ex.getMessage())
        .build();
    return new ResponseEntity<>(apiErrorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidationError(MethodArgumentNotValidException ex) {
    log.error("validation exception", ex);
    var errors = ex.getBindingResult().getAllErrors().stream().map(error -> {
      String field = error instanceof FieldError fe ? fe.getField() : error.getObjectName();
      return ApiErrorResponse.AdditionalError.builder().field(field).message(error.getDefaultMessage()).build();
    }).toList();

    ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .errorMsg("validation error")
        .errors(errors)
        .build();
    return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex) {
    log.error("access denied", ex);
    ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder().status(HttpStatus.FORBIDDEN.value())
        .errorMsg("forbidden")
        .build();
    return new ResponseEntity<>(apiErrorResponse, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiErrorResponse> handleBadCredentials(BadCredentialsException ex) {
    log.error("bad credentials", ex);
    ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder().status(HttpStatus.UNAUTHORIZED.value())
        .errorMsg("invalid credentials")
        .build();
    return new ResponseEntity<>(apiErrorResponse, HttpStatus.UNAUTHORIZED);
  }
}
