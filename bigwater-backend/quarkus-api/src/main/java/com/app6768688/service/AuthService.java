package com.app6768688.service;

import com.app6768688.dto.AuthRequest;
import com.app6768688.dto.AuthResponse;
import com.app6768688.model.User;
import com.app6768688.util.JwtUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AuthService {
    
    @Inject
    UserService userService;
    
    @Inject
    JwtUtil jwtUtil;
    
    @Transactional
    public AuthResponse register(AuthRequest request) {
        try {
            // Validate input
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return new AuthResponse(false, "Email is required");
            }
            
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return new AuthResponse(false, "Password is required");
            }
            
            if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
                return new AuthResponse(false, "First name is required");
            }
            
            if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
                return new AuthResponse(false, "Last name is required");
            }
            
            // Check if email already exists
            if (userService.findByEmail(request.getEmail()).isPresent()) {
                return new AuthResponse(false, "User with this email already exists");
            }
            
            // Create user
            User user = userService.registerUser(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                request.getPhone(),
                request.getReferralCode()
            );
            
            // Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole().name());
            
            // Create user info
            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name(),
                user.getStatus().name()
            );
            
            return new AuthResponse(true, "Registration successful", token, userInfo);
            
        } catch (Exception e) {
            return new AuthResponse(false, "Registration failed: " + e.getMessage());
        }
    }
    
    public AuthResponse login(AuthRequest request) {
        try {
            // Validate input
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return new AuthResponse(false, "Email is required");
            }
            
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return new AuthResponse(false, "Password is required");
            }
            
            // Authenticate user
            User user = userService.authenticateUser(request.getEmail(), request.getPassword());
            
            // Check if user is active
            if (!user.isActive()) {
                return new AuthResponse(false, "Account is not active");
            }
            
            // Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole().name());
            
            // Create user info
            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name(),
                user.getStatus().name()
            );
            
            return new AuthResponse(true, "Login successful", token, userInfo);
            
        } catch (Exception e) {
            return new AuthResponse(false, "Login failed: " + e.getMessage());
        }
    }
    
    public AuthResponse validateToken(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                return new AuthResponse(false, "Token is required");
            }
            
            // Extract user info from token
            String email = jwtUtil.extractUsername(token);
            Long userId = jwtUtil.extractUserId(token);
            String role = jwtUtil.extractRole(token);
            
            // Validate token
            if (!jwtUtil.validateToken(token, email)) {
                return new AuthResponse(false, "Invalid or expired token");
            }
            
            // Get user from database
            User user = userService.findByEmail(email).orElse(null);
            if (user == null) {
                return new AuthResponse(false, "User not found");
            }
            
            if (!user.isActive()) {
                return new AuthResponse(false, "Account is not active");
            }
            
            // Create user info
            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name(),
                user.getStatus().name()
            );
            
            return new AuthResponse(true, "Token is valid", token, userInfo);
            
        } catch (Exception e) {
            return new AuthResponse(false, "Token validation failed: " + e.getMessage());
        }
    }
}
