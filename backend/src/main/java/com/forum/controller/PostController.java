package com.forum.controller;

import com.forum.config.RequireOwnership;
import com.forum.dto.CreatePostRequest;
import com.forum.dto.PostDTO;
import com.forum.service.PostService;
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
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private PermissionService permissionService;
    
    @GetMapping
    public ResponseEntity<Page<PostDTO>> getAllPosts(Pageable pageable) {
        Page<PostDTO> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        PostDTO post = postService.getPostById(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostRequest request, 
                                      Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            PostDTO post = postService.createPost(request, userDetails.getUsername());
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @RequireOwnership(resourceType = "post", resourceIdParam = "id")
    public ResponseEntity<?> updatePost(@PathVariable Long id, 
                                      @Valid @RequestBody CreatePostRequest request,
                                      Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (!permissionService.canEditPost(id, userDetails.getUsername())) {
                return ResponseEntity.status(403).body("您没有权限编辑此帖子");
            }
            
            PostDTO post = postService.updatePost(id, request, userDetails.getUsername());
            if (post != null) {
                return ResponseEntity.ok(post);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @RequireOwnership(resourceType = "post", resourceIdParam = "id")
    public ResponseEntity<?> deletePost(@PathVariable Long id, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (!permissionService.canDeletePost(id, userDetails.getUsername())) {
                return ResponseEntity.status(403).body("您没有权限删除此帖子");
            }
            
            boolean deleted = postService.deletePost(id, userDetails.getUsername());
            if (deleted) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<PostDTO>> getPostsByCategory(@PathVariable Long categoryId, 
                                                          Pageable pageable) {
        Page<PostDTO> posts = postService.getPostsByCategory(categoryId, pageable);
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/user/{username}")
    public ResponseEntity<Page<PostDTO>> getPostsByUser(@PathVariable String username, 
                                                      Pageable pageable) {
        Page<PostDTO> posts = postService.getPostsByUser(username, pageable);
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<PostDTO>> searchPosts(@RequestParam String keyword, 
                                                   Pageable pageable) {
        Page<PostDTO> posts = postService.searchPosts(keyword, pageable);
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/popular")
    public ResponseEntity<List<PostDTO>> getPopularPosts() {
        List<PostDTO> posts = postService.getPopularPosts();
        return ResponseEntity.ok(posts);
    }
    
    @PostMapping("/{id}/like")
    public ResponseEntity<?> likePost(@PathVariable Long id, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            PostDTO post = postService.likePost(id, userDetails.getUsername());
            if (post != null) {
                return ResponseEntity.ok(post);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 