package com.example.blogapp.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.blogapp.domain.entities.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, UUID> {

  @Query("select c from Category c left join fetch c.posts")
  List<Category> findAllWithPostCount();

  Boolean existsByNameIgnoreCase(String name); // the function is a jpa function which return the query result of
                                               // checking the database for the name and return true or false

}
