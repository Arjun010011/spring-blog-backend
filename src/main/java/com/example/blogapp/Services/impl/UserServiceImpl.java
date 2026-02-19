package com.example.blogapp.Services.impl;

import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.blogapp.Services.UserService;
import com.example.blogapp.domain.entities.User;
import com.example.blogapp.repo.UserRepo;
import com.example.blogapp.security.BlogUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepo userRepo;

  @Override
  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()
        || "anonymousUser".equals(authentication.getPrincipal())) {
      throw new AccessDeniedException("unauthorized");
    }

    if (authentication.getPrincipal() instanceof BlogUserDetails details) {
      return details.getUser();
    }

    throw new AccessDeniedException("unauthorized");
  }

  @Override
  public User getUserById(UUID id) {
    return userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("user not found"));
  }
}
