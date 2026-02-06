package com.example.blogapp.Services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.blogapp.Services.CategoryService;
import com.example.blogapp.domain.entities.Category;
import com.example.blogapp.repo.CategoryRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepo categoryRepo;

  @Override
  public List<Category> listCategories() {
    return categoryRepo.findAllWithPostCount();
  }
}
