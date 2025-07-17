package com.forum.dto;

public class UserStatsDTO {
    private Long postCount;
    private Long commentCount;
    private Long likeCount;
    
    public UserStatsDTO() {}
    
    public UserStatsDTO(Long postCount, Long commentCount, Long likeCount) {
        this.postCount = postCount;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
    }
    
    // Getters and Setters
    public Long getPostCount() {
        return postCount;
    }
    
    public void setPostCount(Long postCount) {
        this.postCount = postCount;
    }
    
    public Long getCommentCount() {
        return commentCount;
    }
    
    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }
    
    public Long getLikeCount() {
        return likeCount;
    }
    
    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }
}