package com.forum.repository;

import com.forum.entity.Post;
import com.forum.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByStatus(Post.Status status, Pageable pageable);
    Page<Post> findByAuthor(User author, Pageable pageable);
    Page<Post> findByCategoryId(Long categoryId, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.status = :status ORDER BY p.pinned DESC, p.createdAt DESC")
    Page<Post> findByStatusOrderByPinnedAndCreatedAt(@Param("status") Post.Status status, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    Page<Post> findByTitleOrContentContaining(@Param("keyword") String keyword, Pageable pageable);
    
    List<Post> findTop10ByStatusOrderByViewCountDesc(Post.Status status);
    List<Post> findTop10ByStatusOrderByLikeCountDesc(Post.Status status);
    
    // 统计用户帖子数量
    long countByAuthor(User author);
    
    // 统计用户获得的点赞数
    @Query("SELECT COALESCE(SUM(p.likeCount), 0) FROM Post p WHERE p.author = :author")
    long sumLikeCountByAuthor(@Param("author") User author);
} 