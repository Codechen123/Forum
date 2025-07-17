package com.forum.service;

import com.forum.dto.RegisterRequest;
import com.forum.dto.UserDTO;
import com.forum.dto.UserStatsDTO;
import com.forum.entity.User;
import com.forum.repository.UserRepository;
import com.forum.repository.PostRepository;
import com.forum.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
    }
    
    public UserDTO register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBio(request.getBio());
        
        User savedUser = userRepository.save(user);
        return new UserDTO(savedUser);
    }
    
    public UserDTO getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(UserDTO::new).orElse(null);
    }
    
    public UserDTO getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(UserDTO::new).orElse(null);
    }
    
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }
    
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setBio(userDTO.getBio());
            user.setAvatar(userDTO.getAvatar());
            
            User updatedUser = userRepository.save(user);
            return new UserDTO(updatedUser);
        }
        return null;
    }
    
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public UserStatsDTO getUserStats(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            long postCount = postRepository.countByAuthor(user);
            long commentCount = commentRepository.countByAuthor(user);
            long likeCount = postRepository.sumLikeCountByAuthor(user);
            
            return new UserStatsDTO(postCount, commentCount, likeCount);
        }
        return null;
    }
    
    public UserDTO updateProfile(String username, UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // 只更新允许修改的字段
            if (userDTO.getBio() != null) {
                user.setBio(userDTO.getBio());
            }
            if (userDTO.getAvatar() != null) {
                user.setAvatar(userDTO.getAvatar());
            }
            
            User updatedUser = userRepository.save(user);
            return new UserDTO(updatedUser);
        }
        return null;
    }
} 