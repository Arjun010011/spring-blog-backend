package com.example.blogapp.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.example.blogapp.domain.PostStatus;
import com.example.blogapp.domain.entities.Category;
import com.example.blogapp.domain.entities.Tag;
import com.example.blogapp.dtos.AuthRegisterRequest;
import com.example.blogapp.dtos.CreatePostRequest;
import com.example.blogapp.repo.CategoryRepo;
import com.example.blogapp.repo.TagRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
public class PostControllerTest {

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private CategoryRepo categoryRepo;

  @Autowired
  private TagRepo tagRepo;

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  void publicOnlySeesPublishedPosts() throws Exception {
    Category category = categoryRepo.save(Category.builder().name("Tech News").build());
    Tag tag = tagRepo.save(Tag.builder().name("java").build());

    String token = registerAndGetToken();

    CreatePostRequest draft = CreatePostRequest.builder()
        .title("Draft post")
        .content("draft content")
        .categoryId(category.getId())
        .tagIds(Set.of(tag.getId()))
        .status(PostStatus.DRAFT)
        .build();

    CreatePostRequest published = CreatePostRequest.builder()
        .title("Published post")
        .content("published content")
        .categoryId(category.getId())
        .tagIds(Set.of(tag.getId()))
        .status(PostStatus.PUBLISHED)
        .build();

    mockMvc.perform(post("/api/v1/posts")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(draft)))
        .andExpect(status().isCreated());

    mockMvc.perform(post("/api/v1/posts")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(published)))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/api/v1/posts"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].title").value("Published post"));
  }

  private String registerAndGetToken() throws Exception {
    AuthRegisterRequest registerRequest = AuthRegisterRequest.builder()
        .name("Bob")
        .email("bob@example.com")
        .password("password123")
        .build();

    MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(registerRequest)))
        .andExpect(status().isCreated())
        .andReturn();

    JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
    return json.get("token").asText();
  }
}
