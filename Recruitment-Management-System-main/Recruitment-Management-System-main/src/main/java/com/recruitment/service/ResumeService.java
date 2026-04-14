package com.recruitment.service;

import com.recruitment.dto.ResumeData;
import com.recruitment.model.Profile;
import com.recruitment.model.User;
import com.recruitment.repository.ProfileRepository;
import com.recruitment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ResumeService {
    
    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Value("${resume.parser.api.url}")
    private String apiUrl;
    
    @Value("${resume.parser.api.key}")
    private String apiKey;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    public Profile processResume(MultipartFile file, User user) throws IOException {
        // Validate file type
        String contentType = file.getContentType();
        if (!isValidFileType(contentType)) {
            throw new IllegalArgumentException("Only PDF and DOCX files are allowed");
        }
        
        // Call third-party API
        ResumeData resumeData = callResumeParserAPI(file);
        
        // Create or update profile
        Optional<Profile> existingProfile = profileRepository.findByApplicant(user);
        Profile profile;
        
        if (existingProfile.isPresent()) {
            profile = existingProfile.get();
        } else {
            profile = new Profile();
            profile.setApplicant(user);
        }
        
        // Update profile with extracted data
        profile.setResumeFileAddress(file.getOriginalFilename());
        profile.setName(resumeData.getName() != null ? resumeData.getName() : user.getName());
        profile.setEmail(resumeData.getEmail() != null ? resumeData.getEmail() : user.getEmail());
        profile.setPhone(resumeData.getPhone());
        
        // Convert lists to strings
        if (resumeData.getSkills() != null) {
            profile.setSkills(String.join(", ", resumeData.getSkills()));
        }
        
        if (resumeData.getEducation() != null) {
            StringBuilder educationStr = new StringBuilder();
            for (ResumeData.Education edu : resumeData.getEducation()) {
                educationStr.append(edu.getName());
                if (edu.getUrl() != null) {
                    educationStr.append(" (").append(edu.getUrl()).append(")");
                }
                educationStr.append("; ");
            }
            profile.setEducation(educationStr.toString());
        }
        
        if (resumeData.getExperience() != null) {
            StringBuilder experienceStr = new StringBuilder();
            for (ResumeData.Experience exp : resumeData.getExperience()) {
                experienceStr.append(exp.getName());
                if (exp.getDates() != null && !exp.getDates().isEmpty()) {
                    experienceStr.append(" (").append(String.join(", ", exp.getDates())).append(")");
                }
                if (exp.getUrl() != null) {
                    experienceStr.append(" - ").append(exp.getUrl());
                }
                experienceStr.append("; ");
            }
            profile.setExperience(experienceStr.toString());
        }
        
        return profileRepository.save(profile);
    }
    
    private ResumeData callResumeParserAPI(MultipartFile file) throws IOException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.set("apikey", apiKey);
            
            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            
            HttpEntity<ByteArrayResource> entity = new HttpEntity<>(resource, headers);
            
            ResponseEntity<ResumeData> response = restTemplate.postForEntity(
                apiUrl, 
                entity, 
                ResumeData.class
            );
            
            return response.getBody();
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to process resume: " + e.getMessage());
        }
    }
    
    private boolean isValidFileType(String contentType) {
        return "application/pdf".equals(contentType) || 
               "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(contentType);
    }
    
    public Profile getProfileByApplicantId(Long applicantId) {
        return profileRepository.findByApplicantId(applicantId)
                .orElseThrow(() -> new RuntimeException("Profile not found for applicant: " + applicantId));
    }
}
