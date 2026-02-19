package com.example.blogapp.Services.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.blogapp.Services.PostService;
import com.example.blogapp.Services.UserService;
import com.example.blogapp.domain.PostStatus;
import com.example.blogapp.domain.entities.Category;
import com.example.blogapp.domain.entities.Post;
import com.example.blogapp.domain.entities.Tag;
import com.example.blogapp.domain.entities.User;
import com.example.blogapp.repo.CategoryRepo;
import com.example.blogapp.repo.PostRepo;
import com.example.blogapp.repo.TagRepo;
import com.example.blogapp.repo.specs.PostSpecifications;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private static final int WORDS_PER_MINUTE = 200;

  private final PostRepo postRepo;
  private final CategoryRepo categoryRepo;
  private final TagRepo tagRepo;
  private final UserService userService;

  @Override
  public Page<Post> listPosts(PostStatus status, UUID categoryId, UUID tagId, UUID authorId, String query,
      Pageable pageable) {
    var spec = PostSpecifications.hasCategory(categoryId)
        .and(PostSpecifications.hasTag(tagId))
        .and(PostSpecifications.hasAuthor(authorId))
        .and(PostSpecifications.search(query));

    if (isAnonymous()) {
      spec = spec.and(PostSpecifications.hasStatus(PostStatus.PUBLISHED));
      return postRepo.findAll(spec, pageable);
    }

    User current = userService.getCurrentUser();
    if (status == PostStatus.DRAFT) {
      spec = spec.and(PostSpecifications.hasStatus(PostStatus.DRAFT))
          .and(PostSpecifications.hasAuthor(current.getId()));
    } else if (status == PostStatus.PUBLISHED) {
      spec = spec.and(PostSpecifications.hasStatus(PostStatus.PUBLISHED));
    } else {
      spec = spec.and(PostSpecifications.publishedOrAuthor(current.getId()));
    }

    return postRepo.findAll(spec, pageable);
  }

  @Override
  public Post getPostById(UUID id) {
    Post post = postRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("post not found"));
    if (post.getStatus() == PostStatus.PUBLISHED) {
      return post;
    }

    if (isAnonymous()) {
      throw new AccessDeniedException("forbidden");
    }

    User current = userService.getCurrentUser();
    if (!post.getAuthor().getId().equals(current.getId())) {
      throw new AccessDeniedException("forbidden");
    }

    return post;
  }

  @Override
  @Transactional
  public Post createPost(Post post, UUID categoryId, Set<UUID> tagIds) {
    User author = userService.getCurrentUser();
    Category category = categoryRepo.findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("category not found"));

    post.setAuthor(author);
    post.setCategory(category);
    post.setReadingTime(calculateReadingTime(post.getContent()));
    post.setTags(resolveTags(tagIds));

    return postRepo.save(post);
  }

  @Override
  @Transactional
  public Post updatePost(UUID id, Post updated, UUID categoryId, Set<UUID> tagIds) {
    Post existing = postRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("post not found"));
    User current = userService.getCurrentUser();
    if (!existing.getAuthor().getId().equals(current.getId())) {
      throw new AccessDeniedException("forbidden");
    }

    Category category = categoryRepo.findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("category not found"));

    existing.setTitle(updated.getTitle());
    existing.setContent(updated.getContent());
    existing.setCategory(category);
    existing.setReadingTime(calculateReadingTime(updated.getContent()));
    existing.setTags(resolveTags(tagIds));

    return postRepo.save(existing);
  }

  @Override
  @Transactional
  public Post updatePostStatus(UUID id, PostStatus status) {
    Post existing = postRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("post not found"));
    User current = userService.getCurrentUser();
    if (!existing.getAuthor().getId().equals(current.getId())) {
      throw new AccessDeniedException("forbidden");
    }
    existing.setStatus(status);
    return postRepo.save(existing);
  }

  @Override
  public void deletePost(UUID id) {
    Post existing = postRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("post not found"));
    User current = userService.getCurrentUser();
    if (!existing.getAuthor().getId().equals(current.getId())) {
      throw new AccessDeniedException("forbidden");
    }
    postRepo.deleteById(id);
  }

  private Set<Tag> resolveTags(Set<UUID> tagIds) {
    if (tagIds == null || tagIds.isEmpty()) {
      return new HashSet<>();
    }
    var tags = new HashSet<>(tagRepo.findAllById(tagIds));
    if (tags.size() != tagIds.size()) {
      throw new IllegalArgumentException("one or more tags not found");
    }
    return tags;
  }

  private int calculateReadingTime(String content) {
    if (content == null || content.isBlank()) {
      return 1;
    }
    int words = content.trim().split("\\s+").length;
    int minutes = (int) Math.ceil(words / (double) WORDS_PER_MINUTE);
    return Math.max(minutes, 1);
  }

  private boolean isAnonymous() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication == null || !authentication.isAuthenticated()
        || "anonymousUser".equals(authentication.getPrincipal());
  }
}
