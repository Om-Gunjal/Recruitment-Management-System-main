package com.recruitment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(unique = true, nullable = false)
    private String email;
    
    @NotBlank(message = "Address is required")
    @Column(nullable = false)
    private String address;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "User type is required")
    @Column(nullable = false)
    private UserType userType;
    
    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String passwordHash;
    
    @Column(name = "profile_headline")
    private String profileHeadline;
    
    @OneToOne(mappedBy = "applicant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Profile profile;
    
    @OneToMany(mappedBy = "postedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Job> postedJobs;
    
    @ManyToMany(mappedBy = "applicants", fetch = FetchType.LAZY)
    private List<Job> appliedJobs;
    
    // Constructors
    public User() {}
    
    public User(String name, String email, String address, UserType userType, String passwordHash, String profileHeadline) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.userType = userType;
        this.passwordHash = passwordHash;
        this.profileHeadline = profileHeadline;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public UserType getUserType() {
        return userType;
    }
    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getProfileHeadline() {
        return profileHeadline;
    }
    
    public void setProfileHeadline(String profileHeadline) {
        this.profileHeadline = profileHeadline;
    }
    
    public Profile getProfile() {
        return profile;
    }
    
    public void setProfile(Profile profile) {
        this.profile = profile;
    }
    
    public List<Job> getPostedJobs() {
        return postedJobs;
    }
    
    public void setPostedJobs(List<Job> postedJobs) {
        this.postedJobs = postedJobs;
    }
    
    public List<Job> getAppliedJobs() {
        return appliedJobs;
    }
    
    public void setAppliedJobs(List<Job> appliedJobs) {
        this.appliedJobs = appliedJobs;
    }
    
    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userType.name()));
    }
    
    @Override
    public String getPassword() {
        return passwordHash;
    }
    
    @Override
    public String getUsername() {
        return email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
}
