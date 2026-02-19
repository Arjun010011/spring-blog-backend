package com.example.blogapp.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRegisterRequest {
  @NotBlank(message = "name is required")
  @Size(min = 3, max = 50, message = "should be in the specified limit {min} and {max}")
  private String name;

  @NotBlank(message = "email is required")
  @Email(message = "email should be valid")
  private String email;

  @NotBlank(message = "password is required")
  @Size(min = 6, max = 100, message = "should be in the specified limit {min} and {max}")
  private String password;
}
