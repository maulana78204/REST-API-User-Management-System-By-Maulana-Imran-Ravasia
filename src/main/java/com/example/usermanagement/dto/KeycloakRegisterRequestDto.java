package com.example.usermanagement.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class KeycloakRegisterRequestDto {
    
    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    private String email;
    
    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 8, message = "Password minimal 8 karakter")
    private String password;
    
    @NotBlank(message = "First name tidak boleh kosong")
    @Size(min = 2, max = 50, message = "First name harus 2-50 karakter")
    private String firstName;
    
    @NotBlank(message = "Last name tidak boleh kosong")
    @Size(min = 2, max = 50, message = "Last name harus 2-50 karakter")
    private String lastName;
    
    @NotNull(message = "Role tidak boleh kosong")
    private String role;

    // Constructors
    public KeycloakRegisterRequestDto() {}

    public KeycloakRegisterRequestDto(String email, String password, String firstName, String lastName, String role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    // Getters and Setters
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
