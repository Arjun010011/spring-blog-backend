package com.example.blogapp.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogapp.Services.CategoryService;
import com.example.blogapp.domain.entities.Category;
import com.example.blogapp.dtos.CategoryDto;
import com.example.blogapp.dtos.CreateCategoryRequest;
import com.example.blogapp.mappers.CategoryMapper;

import jakarta.validation.Valid;
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

  @PostMapping
  public ResponseEntity<CategoryDto> createCategories(
      // the functions only runs if the validations are approved
      @Valid
      // it indicates it comes from the request that is the name
      @RequestBody CreateCategoryRequest createCategoryRequest) {
    Category categoryToCreate = categoryMapper.toEntity(createCategoryRequest);
    Category savedCategory = categoryService.createCategory(categoryToCreate);
    return new ResponseEntity<>(categoryMapper.toDto(savedCategory), HttpStatus.CREATED);

  }

}
