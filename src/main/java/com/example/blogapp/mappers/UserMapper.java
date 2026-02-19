package com.example.blogapp.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.blogapp.domain.entities.User;
import com.example.blogapp.dtos.UserDto;
import com.example.blogapp.dtos.UserSummaryDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
  UserDto toDto(User user);

  UserSummaryDto toSummary(User user);
}
