package com.example.blogapp.Services;

import java.util.List;
import java.util.UUID;

import com.example.blogapp.domain.entities.Category;

public interface CategoryService {
  List<Category> listCategories();

  Category createCategory(Category category);

  void deleteCategory(UUID id);

}
