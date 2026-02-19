package com.example.blogapp.repo.specs;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.example.blogapp.domain.PostStatus;
import com.example.blogapp.domain.entities.Post;

import jakarta.persistence.criteria.JoinType;

public class PostSpecifications {

  public static Specification<Post> hasStatus(PostStatus status) {
    return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
  }

  public static Specification<Post> hasCategory(UUID categoryId) {
    return (root, query, cb) -> categoryId == null ? null
        : cb.equal(root.get("category").get("id"), categoryId);
  }

  public static Specification<Post> hasAuthor(UUID authorId) {
    return (root, query, cb) -> authorId == null ? null : cb.equal(root.get("author").get("id"), authorId);
  }

  public static Specification<Post> hasTag(UUID tagId) {
    return (root, query, cb) -> {
      if (tagId == null) {
        return null;
      }
      var join = root.join("tags", JoinType.LEFT);
      query.distinct(true);
      return cb.equal(join.get("id"), tagId);
    };
  }

  public static Specification<Post> search(String queryText) {
    return (root, query, cb) -> {
      if (queryText == null || queryText.isBlank()) {
        return null;
      }
      String like = "%" + queryText.toLowerCase() + "%";
      return cb.or(
          cb.like(cb.lower(root.get("title")), like),
          cb.like(cb.lower(root.get("content")), like));
    };
  }

  public static Specification<Post> publishedOrAuthor(UUID authorId) {
    return (root, query, cb) -> cb.or(
        cb.equal(root.get("status"), PostStatus.PUBLISHED),
        cb.equal(root.get("author").get("id"), authorId));
  }
}
