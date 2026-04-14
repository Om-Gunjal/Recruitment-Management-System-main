package com.recruitment.dto;

import jakarta.validation.constraints.NotBlank;

public class JobRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotBlank(message = "Company name is required")
    private String companyName;
    
    // Constructors
    public JobRequest() {}
    
    public JobRequest(String title, String description, String companyName) {
        this.title = title;
        this.description = description;
        this.companyName = companyName;
    }
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
