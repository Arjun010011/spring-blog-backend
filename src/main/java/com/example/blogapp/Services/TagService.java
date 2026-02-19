package com.example.blogapp.Services;

import java.util.List;
import java.util.UUID;

import com.example.blogapp.domain.entities.Tag;

public interface TagService {
  List<Tag> listTags();

  Tag createTag(Tag tag);

  void deleteTag(UUID id);
}
