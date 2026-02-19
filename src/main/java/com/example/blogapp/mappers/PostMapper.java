package com.example.blogapp.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.blogapp.domain.entities.Post;
import com.example.blogapp.dtos.PostDto;

@Mapper(componentModel = "spring", uses = { UserMapper.class, CategoryMapper.class, TagMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
  PostDto toDto(Post post);
}
