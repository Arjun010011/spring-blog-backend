package com.example.blogapp.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.blogapp.domain.PostStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
  private UUID id;
  private String title;
  private String content;
  private PostStatus status;
  private Integer readingTime;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private UserSummaryDto author;
  private CategorySummaryDto category;
  private List<TagDto> tags;
}
