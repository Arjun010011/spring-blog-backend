package com.example.blogapp.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.blogapp.domain.entities.Tag;
import com.example.blogapp.dtos.TagDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {
  TagDto toDto(Tag tag);
}
