package com.example.blogapp.Services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.blogapp.domain.PostStatus;
import com.example.blogapp.domain.entities.Post;

public interface PostService {
  Page<Post> listPosts(PostStatus status, UUID categoryId, UUID tagId, UUID authorId, String query, Pageable pageable);

  Post getPostById(UUID id);

  Post createPost(Post post, UUID categoryId, java.util.Set<UUID> tagIds);

  Post updatePost(UUID id, Post post, UUID categoryId, java.util.Set<UUID> tagIds);

  Post updatePostStatus(UUID id, PostStatus status);

  void deletePost(UUID id);
}
