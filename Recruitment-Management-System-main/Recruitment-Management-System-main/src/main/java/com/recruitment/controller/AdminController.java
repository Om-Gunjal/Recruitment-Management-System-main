package com.recruitment.controller;

import com.recruitment.dto.JobRequest;
import com.recruitment.model.Job;
import com.recruitment.model.Profile;
import com.recruitment.model.User;
import com.recruitment.service.JobService;
import com.recruitment.service.ResumeService;
import com.recruitment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    
    @Autowired
    private JobService jobService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ResumeService resumeService;
    
    @PostMapping("/job")
    public ResponseEntity<?> createJob(@Valid @RequestBody JobRequest jobRequest,
                                    Authentication authentication) {
        try {
            User admin = (User) authentication.getPrincipal();
            Job job = jobService.createJob(jobRequest, admin);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Job created successfully");
            response.put("jobId", job.getId());
            response.put("title", job.getTitle());
            response.put("companyName", job.getCompanyName());
            response.put("postedOn", job.getPostedOn());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create job: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/job/{jobId}")
    public ResponseEntity<?> getJobDetails(@PathVariable Long jobId,
                                         Authentication authentication) {
        try {
            Optional<Job> jobOpt = jobService.getJobById(jobId);
            
            if (jobOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Job not found");
                return ResponseEntity.notFound().build();
            }
            
            Job job = jobOpt.get();
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", job.getId());
            response.put("title", job.getTitle());
            response.put("description", job.getDescription());
            response.put("companyName", job.getCompanyName());
            response.put("postedOn", job.getPostedOn());
            response.put("totalApplications", job.getTotalApplications());
            response.put("postedBy", job.getPostedBy().getName());
            
            // Add applicants information
            List<Map<String, Object>> applicants = job.getApplicants().stream()
                .map(applicant -> {
                    Map<String, Object> applicantInfo = new HashMap<>();
                    applicantInfo.put("id", applicant.getId());
                    applicantInfo.put("name", applicant.getName());
                    applicantInfo.put("email", applicant.getEmail());
                    applicantInfo.put("profileHeadline", applicant.getProfileHeadline());
                    return applicantInfo;
                })
                .toList();
            
            response.put("applicants", applicants);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch job details: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/applicants")
    public ResponseEntity<?> getAllApplicants(Authentication authentication) {
        try {
            List<User> applicants = userService.getAllApplicants();
            
            List<Map<String, Object>> response = applicants.stream()
                .map(applicant -> {
                    Map<String, Object> applicantInfo = new HashMap<>();
                    applicantInfo.put("id", applicant.getId());
                    applicantInfo.put("name", applicant.getName());
                    applicantInfo.put("email", applicant.getEmail());
                    applicantInfo.put("address", applicant.getAddress());
                    applicantInfo.put("profileHeadline", applicant.getProfileHeadline());
                    applicantInfo.put("userType", applicant.getUserType().name());
                    return applicantInfo;
                })
                .toList();
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch applicants: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/applicant/{applicantId}")
    public ResponseEntity<?> getApplicantDetails(@PathVariable Long applicantId,
                                                Authentication authentication) {
        try {
            User applicant = userService.findById(applicantId);
            Profile profile = resumeService.getProfileByApplicantId(applicantId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("applicant", Map.of(
                "id", applicant.getId(),
                "name", applicant.getName(),
                "email", applicant.getEmail(),
                "address", applicant.getAddress(),
                "profileHeadline", applicant.getProfileHeadline()
            ));
            
            response.put("profile", Map.of(
                "id", profile.getId(),
                "name", profile.getName(),
                "email", profile.getEmail(),
                "phone", profile.getPhone(),
                "skills", profile.getSkills(),
                "education", profile.getEducation(),
                "experience", profile.getExperience(),
                "resumeFileAddress", profile.getResumeFileAddress()
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch applicant details: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
