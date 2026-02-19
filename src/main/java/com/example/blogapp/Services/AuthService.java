package com.example.blogapp.Services;

import com.example.blogapp.dtos.AuthLoginRequest;
import com.example.blogapp.dtos.AuthRegisterRequest;
import com.example.blogapp.dtos.AuthResponse;

public interface AuthService {
  AuthResponse register(AuthRegisterRequest request);

  AuthResponse login(AuthLoginRequest request);
}
