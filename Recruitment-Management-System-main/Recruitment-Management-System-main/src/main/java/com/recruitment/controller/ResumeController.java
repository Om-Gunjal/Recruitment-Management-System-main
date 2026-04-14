package com.recruitment.controller;

import com.recruitment.model.Profile;
import com.recruitment.model.User;
import com.recruitment.service.ResumeService;
import com.recruitment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class ResumeController {
    
    @Autowired
    private ResumeService resumeService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/uploadResume")
    public ResponseEntity<?> uploadResume(@RequestParam("file") MultipartFile file,
                                        Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            
            if (file.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "File is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            Profile profile = resumeService.processResume(file, user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Resume uploaded and processed successfully");
            response.put("profileId", profile.getId());
            response.put("name", profile.getName());
            response.put("email", profile.getEmail());
            response.put("phone", profile.getPhone());
            response.put("skills", profile.getSkills());
            response.put("education", profile.getEducation());
            response.put("experience", profile.getExperience());
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to process file: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "An error occurred: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
