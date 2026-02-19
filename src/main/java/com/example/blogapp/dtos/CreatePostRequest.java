package com.example.blogapp.dtos;

import java.util.Set;
import java.util.UUID;

import com.example.blogapp.domain.PostStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
  @NotBlank(message = "title is required")
  @Size(min = 3, max = 150, message = "should be in the specified limit {min} and {max}")
  private String title;

  @NotBlank(message = "content is required")
  private String content;

  @NotNull(message = "categoryId is required")
  private UUID categoryId;

  private Set<UUID> tagIds;

  @NotNull(message = "status is required")
  private PostStatus status;
}
