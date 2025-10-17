package com.example.usermanagement.controller;

import com.example.usermanagement.dto.KeycloakLoginRequestDto;
import com.example.usermanagement.dto.KeycloakLoginResponseDto;
import com.example.usermanagement.dto.KeycloakRegisterRequestDto;
import com.example.usermanagement.service.KeycloakService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth/keycloak")
@Tag(name = "Keycloak Authentication", description = "API untuk autentikasi menggunakan Keycloak")
public class KeycloakAuthController {

    @Autowired
    private KeycloakService keycloakService;

    @PostMapping("/register")
    @Operation(summary = "Registrasi user dengan Keycloak", description = "Mendaftarkan user baru di Keycloak")
    public ResponseEntity<String> register(@RequestBody @Valid KeycloakRegisterRequestDto dto) {
        String userId = keycloakService.createUser(
            dto.getEmail(),
            dto.getPassword(),
            dto.getFirstName(),
            dto.getLastName(),
            dto.getRole()
        );
        return ResponseEntity.ok("User berhasil didaftarkan di Keycloak dengan ID: " + userId);
    }

    @PostMapping("/login")
    @Operation(summary = "Login dengan Keycloak", description = "Login user dan mendapatkan token dari Keycloak")
    public ResponseEntity<KeycloakLoginResponseDto> login(@RequestBody @Valid KeycloakLoginRequestDto dto) {
        // In a real implementation, you would call Keycloak's token endpoint
        // For now, we'll return a mock response
        KeycloakLoginResponseDto response = new KeycloakLoginResponseDto();
        response.setAccessToken("mock-keycloak-token");
        response.setTokenType("Bearer");
        response.setExpiresIn(3600);
        response.setMessage("Login berhasil. Gunakan token ini untuk mengakses API yang dilindungi.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout dari Keycloak", description = "Logout user dan invalidate token")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        // In a real implementation, you would call Keycloak's logout endpoint
        return ResponseEntity.ok("Logout berhasil dari Keycloak.");
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token Keycloak", description = "Refresh token yang sudah ada")
    public ResponseEntity<KeycloakLoginResponseDto> refresh(@RequestHeader("Authorization") String token) {
        // In a real implementation, you would call Keycloak's refresh token endpoint
        KeycloakLoginResponseDto response = new KeycloakLoginResponseDto();
        response.setAccessToken("refreshed-keycloak-token");
        response.setTokenType("Bearer");
        response.setExpiresIn(3600);
        response.setMessage("Token berhasil di-refresh.");
        return ResponseEntity.ok(response);
    }
}
