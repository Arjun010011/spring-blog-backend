package com.example.blogapp.Services.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.blogapp.Services.AuthService;
import com.example.blogapp.domain.entities.User;
import com.example.blogapp.dtos.AuthLoginRequest;
import com.example.blogapp.dtos.AuthRegisterRequest;
import com.example.blogapp.dtos.AuthResponse;
import com.example.blogapp.mappers.UserMapper;
import com.example.blogapp.repo.UserRepo;
import com.example.blogapp.security.JwtService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public AuthResponse register(AuthRegisterRequest request) {
    if (userRepo.existsByEmailIgnoreCase(request.getEmail())) {
      throw new IllegalArgumentException("email already in use");
    }
    if (userRepo.existsByNameIgnoreCase(request.getName())) {
      throw new IllegalArgumentException("name already in use");
    }

    User user = User.builder()
        .name(request.getName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .build();

    User savedUser = userRepo.save(user);
    String token = jwtService.generateToken(savedUser);

    return AuthResponse.builder()
        .token(token)
        .user(userMapper.toDto(savedUser))
        .build();
  }

  @Override
  public AuthResponse login(AuthLoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    User user = userRepo.findByEmailIgnoreCase(request.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("invalid credentials"));

    String token = jwtService.generateToken(user);
    return AuthResponse.builder()
        .token(token)
        .user(userMapper.toDto(user))
        .build();
  }
}
