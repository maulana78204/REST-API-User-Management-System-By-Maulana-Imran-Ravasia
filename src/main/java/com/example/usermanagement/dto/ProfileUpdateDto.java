package com.example.usermanagement.dto;

import javax.validation.constraints.Size;

public class ProfileUpdateDto {
    
    // Optional: validate length only when provided (null allowed)
    @Size(min = 2, max = 100, message = "Full name harus 2-100 karakter")
    private String fullName;
    
    // Optional: validate length only when provided (null allowed)
    @Size(min = 10, max = 15, message = "Phone harus 10-15 karakter")
    private String phone;
    
    // Optional: validate length only when provided (null allowed)
    @Size(min = 10, max = 255, message = "Address harus 10-255 karakter")
    private String address;
    
    private String avatar;

    // Constructors
    public ProfileUpdateDto() {}

    public ProfileUpdateDto(String fullName, String phone, String address, String avatar) {
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.avatar = avatar;
    }

    // Getters and Setters
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
}