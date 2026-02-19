package com.example.blogapp.Services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.blogapp.Services.TagService;
import com.example.blogapp.domain.entities.Tag;
import com.example.blogapp.repo.TagRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

  private final TagRepo tagRepo;

  @Override
  public List<Tag> listTags() {
    return tagRepo.findAll();
  }

  @Override
  @Transactional
  public Tag createTag(Tag tag) {
    if (tagRepo.existsByNameIgnoreCase(tag.getName())) {
      throw new IllegalArgumentException("tag already exists");
    }
    return tagRepo.save(tag);
  }

  @Override
  public void deleteTag(UUID id) {
    Optional<Tag> curTag = tagRepo.findById(id);
    if (curTag.isPresent()) {
      if (!curTag.get().getPosts().isEmpty()) {
        throw new IllegalStateException("there are posts related to this tag exists");
      }
      tagRepo.deleteById(id);
    }
  }
}
