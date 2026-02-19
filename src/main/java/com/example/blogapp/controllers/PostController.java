package com.example.blogapp.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogapp.Services.PostService;
import com.example.blogapp.domain.PostStatus;
import com.example.blogapp.domain.entities.Post;
import com.example.blogapp.dtos.CreatePostRequest;
import com.example.blogapp.dtos.PostDto;
import com.example.blogapp.dtos.UpdatePostRequest;
import com.example.blogapp.dtos.UpdatePostStatusRequest;
import com.example.blogapp.mappers.PostMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;
  private final PostMapper postMapper;

  @GetMapping
  public ResponseEntity<Page<PostDto>> listPosts(
      @RequestParam(required = false) PostStatus status,
      @RequestParam(required = false) UUID categoryId,
      @RequestParam(required = false) UUID tagId,
      @RequestParam(required = false) UUID authorId,
      @RequestParam(required = false, name = "q") String query,
      @PageableDefault(size = 10) Pageable pageable) {
    Page<PostDto> posts = postService.listPosts(status, categoryId, tagId, authorId, query, pageable)
        .map(postMapper::toDto);
    return ResponseEntity.ok(posts);
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<PostDto> getPost(@PathVariable UUID id) {
    Post post = postService.getPostById(id);
    return ResponseEntity.ok(postMapper.toDto(post));
  }

  @PostMapping
  public ResponseEntity<PostDto> createPost(@Valid @RequestBody CreatePostRequest request) {
    Post post = Post.builder()
        .title(request.getTitle())
        .content(request.getContent())
        .status(request.getStatus())
        .build();

    Post saved = postService.createPost(post, request.getCategoryId(), request.getTagIds());
    return new ResponseEntity<>(postMapper.toDto(saved), HttpStatus.CREATED);
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<PostDto> updatePost(@PathVariable UUID id, @Valid @RequestBody UpdatePostRequest request) {
    Post post = Post.builder()
        .title(request.getTitle())
        .content(request.getContent())
        .build();

    Post updated = postService.updatePost(id, post, request.getCategoryId(), request.getTagIds());
    return ResponseEntity.ok(postMapper.toDto(updated));
  }

  @PatchMapping(path = "/{id}/status")
  public ResponseEntity<PostDto> updateStatus(@PathVariable UUID id,
      @Valid @RequestBody UpdatePostStatusRequest request) {
    Post updated = postService.updatePostStatus(id, request.getStatus());
    return ResponseEntity.ok(postMapper.toDto(updated));
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
    postService.deletePost(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
