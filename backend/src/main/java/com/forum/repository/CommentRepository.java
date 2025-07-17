package com.forum.repository;

import com.forum.entity.Comment;
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
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);
    Page<Comment> findByPost(Post post, Pageable pageable);
    Page<Comment> findByAuthor(User author, Pageable pageable);
    
    @Query("SELECT c FROM Comment c WHERE c.post = :post AND c.parent IS NULL ORDER BY c.createdAt ASC")
    List<Comment> findTopLevelCommentsByPost(@Param("post") Post post);
    
    @Query("SELECT c FROM Comment c WHERE c.parent = :parent ORDER BY c.createdAt ASC")
    List<Comment> findRepliesByParent(@Param("parent") Comment parent);
    
    long countByPost(Post post);
    
    // 统计用户评论数量
    long countByAuthor(User author);
} 