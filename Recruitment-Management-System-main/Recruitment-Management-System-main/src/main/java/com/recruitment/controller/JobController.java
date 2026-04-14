package com.recruitment.controller;

import com.recruitment.model.Job;
import com.recruitment.model.User;
import com.recruitment.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/jobs")
@CrossOrigin(origins = "*")
public class JobController {
    
    @Autowired
    private JobService jobService;
    
    @GetMapping
    public ResponseEntity<?> getAllJobs(Authentication authentication) {
        try {
            List<Job> jobs = jobService.getAllJobs();
            
            List<Map<String, Object>> response = jobs.stream()
                .map(job -> {
                    Map<String, Object> jobInfo = new HashMap<>();
                    jobInfo.put("id", job.getId());
                    jobInfo.put("title", job.getTitle());
                    jobInfo.put("description", job.getDescription());
                    jobInfo.put("companyName", job.getCompanyName());
                    jobInfo.put("postedOn", job.getPostedOn());
                    jobInfo.put("totalApplications", job.getTotalApplications());
                    jobInfo.put("postedBy", job.getPostedBy().getName());
                    return jobInfo;
                })
                .toList();
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch jobs: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/apply")
    public ResponseEntity<?> applyToJob(@RequestParam Long jobId,
                                      Authentication authentication) {
        try {
            User applicant = (User) authentication.getPrincipal();
            Job job = jobService.applyToJob(jobId, applicant);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Successfully applied to job");
            response.put("jobId", job.getId());
            response.put("jobTitle", job.getTitle());
            response.put("companyName", job.getCompanyName());
            response.put("totalApplications", job.getTotalApplications());
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to apply to job: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/my-applications")
    public ResponseEntity<?> getMyApplications(Authentication authentication) {
        try {
            User applicant = (User) authentication.getPrincipal();
            List<Job> appliedJobs = jobService.getAppliedJobsByUser(applicant);
            
            List<Map<String, Object>> response = appliedJobs.stream()
                .map(job -> {
                    Map<String, Object> jobInfo = new HashMap<>();
                    jobInfo.put("id", job.getId());
                    jobInfo.put("title", job.getTitle());
                    jobInfo.put("description", job.getDescription());
                    jobInfo.put("companyName", job.getCompanyName());
                    jobInfo.put("postedOn", job.getPostedOn());
                    jobInfo.put("totalApplications", job.getTotalApplications());
                    jobInfo.put("postedBy", job.getPostedBy().getName());
                    return jobInfo;
                })
                .toList();
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch applied jobs: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
