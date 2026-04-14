package com.recruitment.controller;

import com.recruitment.dto.AuthResponse;
import com.recruitment.dto.LoginRequest;
import com.recruitment.dto.SignupRequest;
import com.recruitment.model.User;
import com.recruitment.security.JwtUtil;
import com.recruitment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            User user = new User(
                signupRequest.getName(),
                signupRequest.getEmail(),
                signupRequest.getAddress(),
                signupRequest.getUserType(),
                signupRequest.getPassword(),
                signupRequest.getProfileHeadline()
            );
            
            User createdUser = userService.createUser(user);
            
            String token = jwtUtil.generateToken(createdUser);
            
            AuthResponse response = new AuthResponse(
                token,
                createdUser.getId(),
                createdUser.getEmail(),
                createdUser.getName(),
                createdUser.getUserType().name()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            User user = (User) authentication.getPrincipal();
            String token = jwtUtil.generateToken(user);
            
            AuthResponse response = new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getUserType().name()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid email or password");
            return ResponseEntity.badRequest().body(error);
        }
    }
}
