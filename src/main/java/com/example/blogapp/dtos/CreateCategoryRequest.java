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
public class CreateCategoryRequest {
  // for validations
  @NotBlank(message = "name is required")
  @Size(min = 5, max = 50, message = "should be in the specified limit {min} and {max}")
  @Pattern(regexp = "^[A-Za-z ]+$", message = "name can contain only letters and spaces")
  private String name;

}
