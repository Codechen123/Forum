package com.forum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreatePostRequest {
    @NotBlank(message = "标题不能为空")
    @Size(min = 5, max = 200, message = "标题长度必须在5-200个字符之间")
    private String title;
    
    @NotBlank(message = "内容不能为空")
    @Size(min = 10, max = 5000, message = "内容长度必须在10-5000个字符之间")
    private String content;
    
    private Long categoryId;
    
    // Constructors
    public CreatePostRequest() {}
    
    public CreatePostRequest(String title, String content, Long categoryId) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
    }
    
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
} 