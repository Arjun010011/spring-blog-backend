package com.example.blogapp.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogapp.Services.UserService;
import com.example.blogapp.domain.entities.User;
import com.example.blogapp.dtos.UserDto;
import com.example.blogapp.mappers.UserMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;

  @GetMapping(path = "/me")
  public ResponseEntity<UserDto> getMe() {
    User user = userService.getCurrentUser();
    return ResponseEntity.ok(userMapper.toDto(user));
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<UserDto> getById(@PathVariable UUID id) {
    User user = userService.getUserById(id);
    return ResponseEntity.ok(userMapper.toDto(user));
  }
}
