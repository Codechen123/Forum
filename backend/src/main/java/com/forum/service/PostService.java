package com.forum.service;

import com.forum.dto.CreatePostRequest;
import com.forum.dto.PostDTO;
import com.forum.entity.Category;
import com.forum.entity.Post;
import com.forum.entity.User;
import com.forum.repository.CategoryRepository;
import com.forum.repository.PostRepository;
import com.forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    public PostDTO createPost(CreatePostRequest request, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(author);
        
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("分类不存在"));
            post.setCategory(category);
        }
        
        Post savedPost = postRepository.save(post);
        return new PostDTO(savedPost);
    }
    
    public Page<PostDTO> getAllPosts(Pageable pageable) {
        return postRepository.findByStatusOrderByPinnedAndCreatedAt(Post.Status.PUBLISHED, pageable)
                .map(PostDTO::new);
    }
    
    public Page<PostDTO> getPostsByCategory(Long categoryId, Pageable pageable) {
        return postRepository.findByCategoryId(categoryId, pageable)
                .map(PostDTO::new);
    }
    
    public Page<PostDTO> getPostsByUser(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return postRepository.findByAuthor(user, pageable)
                .map(PostDTO::new);
    }
    
    public PostDTO getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            Post p = post.get();
            // 增加浏览量
            p.setViewCount(p.getViewCount() + 1);
            postRepository.save(p);
            return new PostDTO(p);
        }
        return null;
    }
    
    public PostDTO updatePost(Long id, CreatePostRequest request, String username) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            
            // 检查是否是作者
            if (!post.getAuthor().getUsername().equals(username)) {
                throw new RuntimeException("无权限编辑此帖子");
            }
            
            post.setTitle(request.getTitle());
            post.setContent(request.getContent());
            
            if (request.getCategoryId() != null) {
                Category category = categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("分类不存在"));
                post.setCategory(category);
            }
            
            Post updatedPost = postRepository.save(post);
            return new PostDTO(updatedPost);
        }
        return null;
    }
    
    public boolean deletePost(Long id, String username) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            
            // 检查是否是作者
            if (!post.getAuthor().getUsername().equals(username)) {
                throw new RuntimeException("无权限删除此帖子");
            }
            
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public Page<PostDTO> searchPosts(String keyword, Pageable pageable) {
        return postRepository.findByTitleOrContentContaining(keyword, pageable)
                .map(PostDTO::new);
    }
    
    public List<PostDTO> getPopularPosts() {
        return postRepository.findTop10ByStatusOrderByViewCountDesc(Post.Status.PUBLISHED)
                .stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }
    
    public PostDTO likePost(Long id, String username) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.setLikeCount(post.getLikeCount() + 1);
            Post updatedPost = postRepository.save(post);
            return new PostDTO(updatedPost);
        }
        return null;
    }
    
    public boolean isPostOwner(Long postId, String username) {
        Optional<Post> post = postRepository.findById(postId);
        return post.isPresent() && post.get().getAuthor().getUsername().equals(username);
    }
} 