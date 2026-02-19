package com.example.blogapp.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogapp.Services.TagService;
import com.example.blogapp.domain.entities.Tag;
import com.example.blogapp.dtos.CreateTagRequest;
import com.example.blogapp.dtos.TagDto;
import com.example.blogapp.mappers.TagMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

  private final TagService tagService;
  private final TagMapper tagMapper;

  @GetMapping
  public ResponseEntity<List<TagDto>> listTags() {
    List<TagDto> tags = tagService.listTags().stream().map(tagMapper::toDto).toList();
    return ResponseEntity.ok(tags);
  }

  @PostMapping
  public ResponseEntity<TagDto> createTag(@Valid @RequestBody CreateTagRequest request) {
    Tag tag = Tag.builder().name(request.getName()).build();
    Tag saved = tagService.createTag(tag);
    return new ResponseEntity<>(tagMapper.toDto(saved), HttpStatus.CREATED);
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
    tagService.deleteTag(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
