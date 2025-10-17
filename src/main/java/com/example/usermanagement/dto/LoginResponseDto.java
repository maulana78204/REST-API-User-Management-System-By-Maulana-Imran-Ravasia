package com.example.usermanagement.dto;

import com.example.usermanagement.entity.RoleEnum;
import com.example.usermanagement.entity.StatusEnum;
import java.util.UUID;

public class LoginResponseDto {
    private String token;
    private UUID userId;
    private String email;
    private String fullName;
    private RoleEnum role;
    private StatusEnum status;

    // Constructor
    public LoginResponseDto() {}

    public LoginResponseDto(String token, UUID userId, String email, String fullName, RoleEnum role, StatusEnum status) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.status = status;
    }

    // getter/setter
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public RoleEnum getRole() { return role; }
    public void setRole(RoleEnum role) { this.role = role; }
    
    public StatusEnum getStatus() { return status; }
    public void setStatus(StatusEnum status) { this.status = status; }
}
