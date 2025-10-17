package com.example.usermanagement.dto;

import com.example.usermanagement.entity.RoleEnum;
import com.example.usermanagement.entity.StatusEnum;

import java.time.LocalDateTime;

public class AdminUserResponseDto {
    private String id;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private String avatar;
    private RoleEnum role;
    private StatusEnum status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public AdminUserResponseDto() {}

    public AdminUserResponseDto(String id, String email, String fullName, String phone, 
                               String address, String avatar, String role, String status,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.avatar = avatar;
        this.role = role != null ? RoleEnum.valueOf(role.toUpperCase()) : null;
        this.status = status != null ? StatusEnum.valueOf(status.toUpperCase()) : null;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public AdminUserResponseDto(RoleEnum role, StatusEnum status, String id, String email, String fullName, String phone, String address, String avatar, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.role = role;
        this.status = status;
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.avatar = avatar;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}