package com.forum.config;

import com.forum.service.UserService;
import com.forum.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                    @NonNull HttpServletResponse response, 
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            
            if (StringUtils.hasText(jwt) && SecurityContextHolder.getContext().getAuthentication() == null) {
                validateTokenAndSetAuthentication(jwt, request);
            }
        } catch (ExpiredJwtException ex) {
            logger.error("JWT token已过期: {}", ex.getMessage());
            handleAuthenticationError(response, "JWT token已过期", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (SignatureException ex) {
            logger.error("JWT token签名无效: {}", ex.getMessage());
            handleAuthenticationError(response, "JWT token签名无效", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (Exception ex) {
            logger.error("JWT token验证失败: {}", ex.getMessage());
            handleAuthenticationError(response, "JWT token验证失败", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void validateTokenAndSetAuthentication(String jwt, HttpServletRequest request) {
        String username = jwtUtil.getUsernameFromToken(jwt);
        
        if (username != null) {
            try {
                UserDetails userDetails = userService.loadUserByUsername(username);
                
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    logger.debug("用户 {} 认证成功", username);
                }
            } catch (UsernameNotFoundException ex) {
                logger.error("用户不存在: {}", username);
            }
        }
    }

    private void handleAuthenticationError(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format("{\"error\": \"%s\", \"status\": %d}", message, statusCode));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        
        // 对于这些路径不进行JWT验证
        return path.startsWith("/api/auth/login") || 
               path.startsWith("/api/auth/register") ||
               (path.startsWith("/api/") && "GET".equals(request.getMethod()) && 
                (path.contains("/posts") || path.contains("/categories") || path.contains("/comments")) &&
                !path.contains("/me")) || // 排除包含 /me 的个人接口
               // 允许访问其他用户的公开信息（非 /me 接口）
               (path.startsWith("/api/users/") && "GET".equals(request.getMethod()) && 
                !path.contains("/me") && (path.matches(".*/users/[^/]+/?$") || path.matches(".*/users/[^/]+/stats$")));
    }
} 