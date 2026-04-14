package com.recruitment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ResumeData {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("phone")
    private String phone;
    
    @JsonProperty("skills")
    private List<String> skills;
    
    @JsonProperty("education")
    private List<Education> education;
    
    @JsonProperty("experience")
    private List<Experience> experience;
    
    // Constructors
    public ResumeData() {}
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public List<String> getSkills() {
        return skills;
    }
    
    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
    
    public List<Education> getEducation() {
        return education;
    }
    
    public void setEducation(List<Education> education) {
        this.education = education;
    }
    
    public List<Experience> getExperience() {
        return experience;
    }
    
    public void setExperience(List<Experience> experience) {
        this.experience = experience;
    }
    
    // Inner classes
    public static class Education {
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("url")
        private String url;
        
        // Constructors
        public Education() {}
        
        // Getters and Setters
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
    }
    
    public static class Experience {
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("dates")
        private List<String> dates;
        
        @JsonProperty("url")
        private String url;
        
        // Constructors
        public Experience() {}
        
        // Getters and Setters
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public List<String> getDates() {
            return dates;
        }
        
        public void setDates(List<String> dates) {
            this.dates = dates;
        }
        
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
    }
}
