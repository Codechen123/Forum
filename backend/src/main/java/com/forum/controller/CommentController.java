package com.forum.controller;

import com.forum.config.RequireOwnership;
import com.forum.dto.CommentDTO;
import com.forum.dto.CreateCommentRequest;
import com.forum.service.CommentService;
import com.forum.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "http://localhost:3000")
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private PermissionService permissionService;
    
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentDTO> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }
    
    @GetMapping("/post/{postId}/page")
    public ResponseEntity<Page<CommentDTO>> getCommentsByPost(@PathVariable Long postId, 
                                                            Pageable pageable) {
        Page<CommentDTO> comments = commentService.getCommentsByPost(postId, pageable);
        return ResponseEntity.ok(comments);
    }
    
    @GetMapping("/user/{username}")
    public ResponseEntity<Page<CommentDTO>> getCommentsByUser(@PathVariable String username, 
                                                            Pageable pageable) {
        Page<CommentDTO> comments = commentService.getCommentsByUser(username, pageable);
        return ResponseEntity.ok(comments);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        CommentDTO comment = commentService.getCommentById(id);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<?> createComment(@Valid @RequestBody CreateCommentRequest request, 
                                         Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            CommentDTO comment = commentService.createComment(request, userDetails.getUsername());
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @RequireOwnership(resourceType = "comment", resourceIdParam = "id")
    public ResponseEntity<?> updateComment(@PathVariable Long id, 
                                         @Valid @RequestBody CreateCommentRequest request,
                                         Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (!permissionService.canEditComment(id, userDetails.getUsername())) {
                return ResponseEntity.status(403).body("您没有权限编辑此评论");
            }
            
            CommentDTO comment = commentService.updateComment(id, request, userDetails.getUsername());
            if (comment != null) {
                return ResponseEntity.ok(comment);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @RequireOwnership(resourceType = "comment", resourceIdParam = "id")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (!permissionService.canDeleteComment(id, userDetails.getUsername())) {
                return ResponseEntity.status(403).body("您没有权限删除此评论");
            }
            
            boolean deleted = commentService.deleteComment(id, userDetails.getUsername());
            if (deleted) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeComment(@PathVariable Long id, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            CommentDTO comment = commentService.likeComment(id, userDetails.getUsername());
            if (comment != null) {
                return ResponseEntity.ok(comment);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/{parentId}/replies")
    public ResponseEntity<List<CommentDTO>> getRepliesByParent(@PathVariable Long parentId) {
        List<CommentDTO> replies = commentService.getRepliesByParent(parentId);
        return ResponseEntity.ok(replies);
    }
} 