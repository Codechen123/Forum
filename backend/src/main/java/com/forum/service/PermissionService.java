package com.forum.service;

import com.forum.entity.Comment;
import com.forum.entity.Post;
import com.forum.entity.User;
import com.forum.repository.CommentRepository;
import com.forum.repository.PostRepository;
import com.forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    /**
     * 获取当前认证用户
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 检查用户是否为管理员
     */
    public boolean isAdmin(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() && user.get().getRole() == User.Role.ADMIN;
    }

    /**
     * 检查用户是否为版主或管理员
     */
    public boolean isModerator(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() && 
               (user.get().getRole() == User.Role.MODERATOR || user.get().getRole() == User.Role.ADMIN);
    }

    /**
     * 检查用户是否可以编辑帖子
     */
    public boolean canEditPost(Long postId, String username) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            return false;
        }

        // 帖子作者可以编辑
        if (post.get().getAuthor().getUsername().equals(username)) {
            return true;
        }

        // 管理员和版主可以编辑任何帖子
        return isModerator(username);
    }

    /**
     * 检查用户是否可以删除帖子
     */
    public boolean canDeletePost(Long postId, String username) {
        return canEditPost(postId, username);
    }

    /**
     * 检查用户是否可以编辑评论
     */
    public boolean canEditComment(Long commentId, String username) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) {
            return false;
        }

        // 评论作者可以编辑
        if (comment.get().getAuthor().getUsername().equals(username)) {
            return true;
        }

        // 管理员和版主可以编辑任何评论
        return isModerator(username);
    }

    /**
     * 检查用户是否可以删除评论
     */
    public boolean canDeleteComment(Long commentId, String username) {
        return canEditComment(commentId, username);
    }

    /**
     * 检查用户是否可以置顶帖子
     */
    public boolean canPinPost(String username) {
        return isModerator(username);
    }

    /**
     * 检查用户是否可以锁定帖子
     */
    public boolean canLockPost(String username) {
        return isModerator(username);
    }

    /**
     * 检查用户是否可以管理用户
     */
    public boolean canManageUser(String username) {
        return isAdmin(username);
    }

    /**
     * 检查用户是否可以管理分类
     */
    public boolean canManageCategory(String username) {
        return isModerator(username);
    }

    /**
     * 检查用户是否可以查看管理面板
     */
    public boolean canAccessAdminPanel(String username) {
        return isModerator(username);
    }

    /**
     * 检查用户是否可以编辑其他用户的资料
     */
    public boolean canEditUserProfile(Long userId, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return false;
        }

        // 用户可以编辑自己的资料
        if (user.get().getId().equals(userId)) {
            return true;
        }

        // 管理员可以编辑任何用户的资料
        return isAdmin(username);
    }

    /**
     * 检查用户是否可以删除其他用户
     */
    public boolean canDeleteUser(Long userId, String username) {
        Optional<User> targetUser = userRepository.findById(userId);
        Optional<User> currentUser = userRepository.findByUsername(username);
        
        if (targetUser.isEmpty() || currentUser.isEmpty()) {
            return false;
        }

        // 管理员可以删除任何用户（除了自己）
        return currentUser.get().getRole() == User.Role.ADMIN &&
                !currentUser.get().getId().equals(userId);
    }

    /**
     * 检查用户是否可以禁用其他用户
     */
    public boolean canBanUser(Long userId, String username) {
        Optional<User> targetUser = userRepository.findById(userId);
        Optional<User> currentUser = userRepository.findByUsername(username);
        
        if (targetUser.isEmpty() || currentUser.isEmpty()) {
            return false;
        }

        // 管理员和版主可以禁用普通用户
        if (isModerator(username) && targetUser.get().getRole() == User.Role.USER) {
            return true;
        }

        // 管理员可以禁用版主
        if (isAdmin(username) && targetUser.get().getRole() == User.Role.MODERATOR) {
            return true;
        }

        return false;
    }
} 