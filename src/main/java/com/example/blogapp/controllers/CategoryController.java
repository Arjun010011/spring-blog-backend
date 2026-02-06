package com.example.blogapp.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogapp.Services.CategoryService;
import com.example.blogapp.dtos.CategoryDto;
import com.example.blogapp.mappers.CategoryMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;
  private final CategoryMapper categoryMapper;

  @GetMapping
  public ResponseEntity<List<CategoryDto>> listCategories() {
    List<CategoryDto> categories = categoryService.listCategories().stream()
        .map(category -> categoryMapper.toDto(category)).toList();
    return ResponseEntity.ok(categories);
  }

}
