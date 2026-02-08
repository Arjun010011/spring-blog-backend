package com.example.blogapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

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
}
