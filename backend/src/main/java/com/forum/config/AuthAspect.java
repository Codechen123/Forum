package com.forum.config;

import com.forum.entity.User;
import com.forum.service.CommentService;
import com.forum.service.PostService;
import com.forum.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class AuthAspect {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Before("@annotation(requireRole)")
    public void checkRole(JoinPoint joinPoint, RequireRole requireRole) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("用户未认证");
        }

        User currentUser = (User) authentication.getPrincipal();
        User.Role userRole = currentUser.getRole();
        
        boolean hasRole = false;
        for (User.Role allowedRole : requireRole.value()) {
            if (userRole == allowedRole) {
                hasRole = true;
                break;
            }
        }
        
        if (!hasRole) {
            throw new AccessDeniedException(requireRole.message());
        }
    }

    @Before("@annotation(requireOwnership)")
    public void checkOwnership(JoinPoint joinPoint, RequireOwnership requireOwnership) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("用户未认证");
        }

        User currentUser = (User) authentication.getPrincipal();
        
        // 管理员和版主可以操作所有资源
        if (currentUser.getRole() == User.Role.ADMIN || currentUser.getRole() == User.Role.MODERATOR) {
            return;
        }

        // 获取资源ID
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] paramNames = signature.getParameterNames();
        
        Long resourceId = null;
        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i].equals(requireOwnership.resourceIdParam())) {
                resourceId = (Long) args[i];
                break;
            }
        }

        if (resourceId == null) {
            throw new IllegalArgumentException("无法找到资源ID参数");
        }

        // 验证所有权
        boolean isOwner = false;
        switch (requireOwnership.resourceType().toLowerCase()) {
            case "post":
                isOwner = postService.isPostOwner(resourceId, currentUser.getUsername());
                break;
            case "comment":
                isOwner = commentService.isCommentOwner(resourceId, currentUser.getUsername());
                break;
            case "user":
                isOwner = resourceId.equals(currentUser.getId());
                break;
            default:
                throw new IllegalArgumentException("不支持的资源类型: " + requireOwnership.resourceType());
        }

        if (!isOwner) {
            throw new AccessDeniedException(requireOwnership.message());
        }
    }
} 