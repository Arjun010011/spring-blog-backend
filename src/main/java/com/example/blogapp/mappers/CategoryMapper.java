package com.example.blogapp.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.example.blogapp.domain.PostStatus;
import com.example.blogapp.domain.entities.Category;
import com.example.blogapp.domain.entities.Post;
import com.example.blogapp.dtos.CategoryDto;
import com.example.blogapp.dtos.CreateCategoryRequest;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
  @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
  CategoryDto toDto(Category Category);

  Category toEntity(CreateCategoryRequest createCategoryRequest);

  @Named("calculatePostCount")
  default long calculatePostCount(List<Post> posts) {
    if (null == posts) {
      return 0;
    }
    return posts.stream().filter(post -> PostStatus.PUBLISHED.equals(post.getStatus())).count();
  }
}
