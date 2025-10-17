package com.example.usermanagement.dto;

import com.example.usermanagement.entity.RoleEnum;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AdminUserUpdateDto {
    
    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    private String email;
    
    @NotBlank(message = "Full name tidak boleh kosong")
    @Size(min = 2, max = 100, message = "Full name harus 2-100 karakter")
    private String fullName;
    
    @NotBlank(message = "Phone tidak boleh kosong")
    @Size(min = 10, max = 15, message = "Phone harus 10-15 karakter")
    private String phone;
    
    @NotBlank(message = "Address tidak boleh kosong")
    @Size(min = 10, max = 255, message = "Address harus 10-255 karakter")
    private String address;
    
    private String avatar;
    
    @NotNull(message = "Role tidak boleh kosong")
    private RoleEnum role;

    // Constructors
    public AdminUserUpdateDto() {}

    public AdminUserUpdateDto(String email, String fullName, String phone, String address, 
                             String avatar, RoleEnum role) {
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.avatar = avatar;
        this.role = role;
    }

    // Getters and Setters
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
}