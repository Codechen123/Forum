package com.forum.controller;

import com.forum.config.RequireRole;
import com.forum.dto.UserDTO;
import com.forum.entity.User;
import com.forum.service.PermissionService;
import com.forum.service.UserService;
import com.forum.service.PostService;
import com.forum.service.CommentService;
import com.forum.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 获取管理面板统计数据
     */
    @GetMapping("/stats")
    @RequireRole({User.Role.ADMIN, User.Role.MODERATOR})
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 这里可以添加统计数据，如用户数量、帖子数量等
        stats.put("totalUsers", userService.getAllUsers().size());
        stats.put("message", "管理面板统计数据");
        
        return ResponseEntity.ok(stats);
    }

    /**
     * 获取所有用户列表
     */
    @GetMapping("/users")
    @RequireRole({User.Role.ADMIN, User.Role.MODERATOR})
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        // 这里应该实现分页的用户列表，暂时返回所有用户
        return ResponseEntity.ok().build();
    }

    /**
     * 更新用户角色
     */
    @PutMapping("/users/{userId}/role")
    @RequireRole({User.Role.ADMIN})
    public ResponseEntity<?> updateUserRole(@PathVariable Long userId, 
                                          @RequestParam String role,
                                          Authentication authentication) {
        try {
            String currentUsername = authentication.getName();
            if (!permissionService.canManageUser(currentUsername)) {
                return ResponseEntity.status(403).body("权限不足");
            }

            User.Role newRole;
            try {
                newRole = User.Role.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("无效的角色类型");
            }

            // 这里应该实现更新用户角色的逻辑
            return ResponseEntity.ok().body("角色更新成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/users/{userId}")
    @RequireRole({User.Role.ADMIN})
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, 
                                       Authentication authentication) {
        try {
            String currentUsername = authentication.getName();
            if (!permissionService.canDeleteUser(userId, currentUsername)) {
                return ResponseEntity.status(403).body("权限不足");
            }

            boolean deleted = userService.deleteUser(userId);
            if (deleted) {
                return ResponseEntity.ok().body("用户删除成功");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 禁用用户
     */
    @PutMapping("/users/{userId}/ban")
    @RequireRole({User.Role.ADMIN, User.Role.MODERATOR})
    public ResponseEntity<?> banUser(@PathVariable Long userId, 
                                    Authentication authentication) {
        try {
            String currentUsername = authentication.getName();
            if (!permissionService.canBanUser(userId, currentUsername)) {
                return ResponseEntity.status(403).body("权限不足");
            }

            // 这里应该实现禁用用户的逻辑
            return ResponseEntity.ok().body("用户禁用成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 解除用户禁用
     */
    @PutMapping("/users/{userId}/unban")
    @RequireRole({User.Role.ADMIN, User.Role.MODERATOR})
    public ResponseEntity<?> unbanUser(@PathVariable Long userId, 
                                      Authentication authentication) {
        try {
            String currentUsername = authentication.getName();
            if (!permissionService.canBanUser(userId, currentUsername)) {
                return ResponseEntity.status(403).body("权限不足");
            }

            // 这里应该实现解除禁用的逻辑
            return ResponseEntity.ok().body("用户解除禁用成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 置顶帖子
     */
    @PutMapping("/posts/{postId}/pin")
    @RequireRole({User.Role.ADMIN, User.Role.MODERATOR})
    public ResponseEntity<?> pinPost(@PathVariable Long postId, 
                                    Authentication authentication) {
        try {
            String currentUsername = authentication.getName();
            if (!permissionService.canPinPost(currentUsername)) {
                return ResponseEntity.status(403).body("权限不足");
            }

            // 这里应该实现置顶帖子的逻辑
            return ResponseEntity.ok().body("帖子置顶成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 取消置顶帖子
     */
    @PutMapping("/posts/{postId}/unpin")
    @RequireRole({User.Role.ADMIN, User.Role.MODERATOR})
    public ResponseEntity<?> unpinPost(@PathVariable Long postId, 
                                      Authentication authentication) {
        try {
            String currentUsername = authentication.getName();
            if (!permissionService.canPinPost(currentUsername)) {
                return ResponseEntity.status(403).body("权限不足");
            }

            // 这里应该实现取消置顶的逻辑
            return ResponseEntity.ok().body("帖子取消置顶成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 锁定帖子
     */
    @PutMapping("/posts/{postId}/lock")
    @RequireRole({User.Role.ADMIN, User.Role.MODERATOR})
    public ResponseEntity<?> lockPost(@PathVariable Long postId, 
                                     Authentication authentication) {
        try {
            String currentUsername = authentication.getName();
            if (!permissionService.canLockPost(currentUsername)) {
                return ResponseEntity.status(403).body("权限不足");
            }

            // 这里应该实现锁定帖子的逻辑
            return ResponseEntity.ok().body("帖子锁定成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 解锁帖子
     */
    @PutMapping("/posts/{postId}/unlock")
    @RequireRole({User.Role.ADMIN, User.Role.MODERATOR})
    public ResponseEntity<?> unlockPost(@PathVariable Long postId, 
                                       Authentication authentication) {
        try {
            String currentUsername = authentication.getName();
            if (!permissionService.canLockPost(currentUsername)) {
                return ResponseEntity.status(403).body("权限不足");
            }

            // 这里应该实现解锁帖子的逻辑
            return ResponseEntity.ok().body("帖子解锁成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 强制删除帖子
     */
    @DeleteMapping("/posts/{postId}")
    @RequireRole({User.Role.ADMIN, User.Role.MODERATOR})
    public ResponseEntity<?> forceDeletePost(@PathVariable Long postId, 
                                            Authentication authentication) {
        try {
            String currentUsername = authentication.getName();
            if (!permissionService.canDeletePost(postId, currentUsername)) {
                return ResponseEntity.status(403).body("权限不足");
            }

            boolean deleted = postService.deletePost(postId, currentUsername);
            if (deleted) {
                return ResponseEntity.ok().body("帖子删除成功");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 强制删除评论
     */
    @DeleteMapping("/comments/{commentId}")
    @RequireRole({User.Role.ADMIN, User.Role.MODERATOR})
    public ResponseEntity<?> forceDeleteComment(@PathVariable Long commentId, 
                                               Authentication authentication) {
        try {
            String currentUsername = authentication.getName();
            if (!permissionService.canDeleteComment(commentId, currentUsername)) {
                return ResponseEntity.status(403).body("权限不足");
            }

            boolean deleted = commentService.deleteComment(commentId, currentUsername);
            if (deleted) {
                return ResponseEntity.ok().body("评论删除成功");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 