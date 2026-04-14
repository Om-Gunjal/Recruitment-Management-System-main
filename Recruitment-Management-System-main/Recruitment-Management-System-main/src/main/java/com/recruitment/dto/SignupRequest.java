package com.recruitment.dto;

import com.recruitment.model.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SignupRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    @NotNull(message = "User type is required")
    private UserType userType;
    
    @NotBlank(message = "Profile headline is required")
    private String profileHeadline;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    // Constructors
    public SignupRequest() {}
    
    public SignupRequest(String name, String email, String password, UserType userType, 
                        String profileHeadline, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.profileHeadline = profileHeadline;
        this.address = address;
    }
    
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public UserType getUserType() {
        return userType;
    }
    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
    public String getProfileHeadline() {
        return profileHeadline;
    }
    
    public void setProfileHeadline(String profileHeadline) {
        this.profileHeadline = profileHeadline;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
}
