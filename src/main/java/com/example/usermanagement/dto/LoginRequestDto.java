package com.example.usermanagement.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class LoginRequestDto {
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    private String password;

    // getter/setter
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
