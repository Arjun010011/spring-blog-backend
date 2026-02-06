package com.example.blogapp.Services;

import java.util.List;

import com.example.blogapp.domain.entities.Category;

public interface CategoryService {
  List<Category> listCategories();
}
