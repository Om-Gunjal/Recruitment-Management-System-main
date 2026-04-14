package com.recruitment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "posted_on", nullable = false)
    private LocalDateTime postedOn;
    
    @Column(name = "total_applications")
    private Integer totalApplications = 0;
    
    @NotBlank(message = "Company name is required")
    @Column(name = "company_name", nullable = false)
    private String companyName;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by", nullable = false)
    @NotNull(message = "Posted by is required")
    private User postedBy;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "job_applications",
        joinColumns = @JoinColumn(name = "job_id"),
        inverseJoinColumns = @JoinColumn(name = "applicant_id")
    )
    private List<User> applicants = new ArrayList<>();
    
    // Constructors
    public Job() {}
    
    public Job(String title, String description, String companyName, User postedBy) {
        this.title = title;
        this.description = description;
        this.companyName = companyName;
        this.postedBy = postedBy;
        this.postedOn = LocalDateTime.now();
        this.totalApplications = 0;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public LocalDateTime getPostedOn() {
        return postedOn;
    }
    
    public void setPostedOn(LocalDateTime postedOn) {
        this.postedOn = postedOn;
    }
    
    public Integer getTotalApplications() {
        return totalApplications;
    }
    
    public void setTotalApplications(Integer totalApplications) {
        this.totalApplications = totalApplications;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public User getPostedBy() {
        return postedBy;
    }
    
    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy;
    }
    
    public List<User> getApplicants() {
        return applicants;
    }
    
    public void setApplicants(List<User> applicants) {
        this.applicants = applicants;
    }
    
    // Helper methods
    public void addApplicant(User applicant) {
        if (!applicants.contains(applicant)) {
            applicants.add(applicant);
            totalApplications = applicants.size();
        }
    }
    
    public void removeApplicant(User applicant) {
        applicants.remove(applicant);
        totalApplications = applicants.size();
    }
}
