package com.example.blogapp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTagRequest {
  @NotBlank(message = "name is required")
  @Size(min = 2, max = 30, message = "should be in the specified limit {min} and {max}")
  @Pattern(regexp = "^[A-Za-z0-9 _-]+$", message = "name can contain letters, numbers, spaces, underscores, and hyphens")
  private String name;
}
