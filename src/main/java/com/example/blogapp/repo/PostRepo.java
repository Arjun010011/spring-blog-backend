package com.example.blogapp.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blogapp.domain.entities.Post;

@Repository
public interface PostRepo extends JpaRepository<Post, UUID> {

}
