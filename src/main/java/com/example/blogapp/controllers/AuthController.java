package com.example.blogapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogapp.Services.AuthService;
import com.example.blogapp.dtos.AuthLoginRequest;
import com.example.blogapp.dtos.AuthRegisterRequest;
import com.example.blogapp.dtos.AuthResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping(path = "/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRegisterRequest request) {
    AuthResponse response = authService.register(request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PostMapping(path = "/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginRequest request) {
    AuthResponse response = authService.login(request);
    return ResponseEntity.ok(response);
  }
}
