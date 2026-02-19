package com.example.blogapp.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthLoginRequest {
  @NotBlank(message = "email is required")
  @Email(message = "email should be valid")
  private String email;

  @NotBlank(message = "password is required")
  private String password;
}
