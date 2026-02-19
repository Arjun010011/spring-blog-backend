package com.example.blogapp.Services;

import java.util.UUID;

import com.example.blogapp.domain.entities.User;

public interface UserService {
  User getCurrentUser();

  User getUserById(UUID id);
}
