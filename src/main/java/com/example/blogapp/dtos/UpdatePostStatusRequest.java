package com.example.blogapp.dtos;

import com.example.blogapp.domain.PostStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostStatusRequest {
  @NotNull(message = "status is required")
  private PostStatus status;
}
