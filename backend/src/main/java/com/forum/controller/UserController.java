package com.forum.controller;

import com.forum.dto.UserDTO;
import com.forum.dto.UserStatsDTO;
import com.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{username}/stats")
    public ResponseEntity<UserStatsDTO> getUserStats(@PathVariable String username) {
        UserStatsDTO stats = userService.getUserStats(username);
        if (stats != null) {
            return ResponseEntity.ok(stats);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/me/stats")
    public ResponseEntity<UserStatsDTO> getCurrentUserStats(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserStatsDTO stats = userService.getUserStats(userDetails.getUsername());
        
        if (stats != null) {
            return ResponseEntity.ok(stats);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/me/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UserDTO userDTO, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserDTO updatedUser = userService.updateProfile(userDetails.getUsername(), userDTO);
            if (updatedUser != null) {
                return ResponseEntity.ok(updatedUser);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}