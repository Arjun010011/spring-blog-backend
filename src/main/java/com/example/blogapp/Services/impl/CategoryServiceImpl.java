package com.example.blogapp.Services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.blogapp.Services.CategoryService;
import com.example.blogapp.domain.entities.Category;
import com.example.blogapp.repo.CategoryRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepo categoryRepo;

  @Override
  public List<Category> listCategories() {
    return categoryRepo.findAllWithPostCount();
  }

  @Override
  @Transactional
  // we used transaction since it interact with db
  public Category createCategory(Category category) {
    // used to check weather the name already exist
    if (categoryRepo.existsByNameIgnoreCase(category.getName())) {
      throw new IllegalArgumentException("Category already exists");
    }
    // returns the saved category
    return categoryRepo.save(category);
  }

}
