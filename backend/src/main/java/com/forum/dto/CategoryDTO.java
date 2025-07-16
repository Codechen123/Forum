package com.forum.dto;

import com.forum.entity.Category;

import java.time.LocalDateTime;

public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private String color;
    private int postCount;
    private LocalDateTime createdAt;
    
    // Constructors
    public CategoryDTO() {}
    
    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.color = category.getColor();
        this.postCount = category.getPosts() != null ? category.getPosts().size() : 0;
        this.createdAt = category.getCreatedAt();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public int getPostCount() { return postCount; }
    public void setPostCount(int postCount) { this.postCount = postCount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 