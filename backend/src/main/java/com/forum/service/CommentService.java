package com.forum.service;

import com.forum.dto.CommentDTO;
import com.forum.dto.CreateCommentRequest;
import com.forum.entity.Comment;
import com.forum.entity.Post;
import com.forum.entity.User;
import com.forum.repository.CommentRepository;
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
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public CommentDTO createComment(CreateCommentRequest request, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("帖子不存在"));
        
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setPost(post);
        comment.setAuthor(author);
        
        // 如果是回复评论
        if (request.getParentId() != null) {
            Comment parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("父评论不存在"));
            comment.setParent(parent);
        }
        
        Comment savedComment = commentRepository.save(comment);
        return new CommentDTO(savedComment);
    }
    
    public List<CommentDTO> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("帖子不存在"));
        
        return commentRepository.findTopLevelCommentsByPost(post)
                .stream()
                .map(CommentDTO::new)
                .collect(Collectors.toList());
    }
    
    public Page<CommentDTO> getCommentsByPost(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("帖子不存在"));
        
        return commentRepository.findByPost(post, pageable)
                .map(CommentDTO::new);
    }
    
    public Page<CommentDTO> getCommentsByUser(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        return commentRepository.findByAuthor(user, pageable)
                .map(CommentDTO::new);
    }
    
    public CommentDTO getCommentById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.map(CommentDTO::new).orElse(null);
    }
    
    public CommentDTO updateComment(Long id, CreateCommentRequest request, String username) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            
            // 检查是否是作者
            if (!comment.getAuthor().getUsername().equals(username)) {
                throw new RuntimeException("无权限编辑此评论");
            }
            
            comment.setContent(request.getContent());
            Comment updatedComment = commentRepository.save(comment);
            return new CommentDTO(updatedComment);
        }
        return null;
    }
    
    public boolean deleteComment(Long id, String username) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            
            // 检查是否是作者
            if (!comment.getAuthor().getUsername().equals(username)) {
                throw new RuntimeException("无权限删除此评论");
            }
            
            commentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public CommentDTO likeComment(Long id, String username) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            comment.setLikeCount(comment.getLikeCount() + 1);
            Comment updatedComment = commentRepository.save(comment);
            return new CommentDTO(updatedComment);
        }
        return null;
    }
    
    public List<CommentDTO> getRepliesByParent(Long parentId) {
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("父评论不存在"));
        
        return commentRepository.findRepliesByParent(parent)
                .stream()
                .map(CommentDTO::new)
                .collect(Collectors.toList());
    }
    
    public boolean isCommentOwner(Long commentId, String username) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        return comment.isPresent() && comment.get().getAuthor().getUsername().equals(username);
    }
} 