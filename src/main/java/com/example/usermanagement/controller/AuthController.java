package com.example.usermanagement.controller;

import com.example.usermanagement.dto.LoginRequestDto;
import com.example.usermanagement.dto.LoginResponseDto;
import com.example.usermanagement.dto.RegisterRequestDto;
import com.example.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "API untuk autentikasi dan otorisasi")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Registrasi user baru", description = "Mendaftarkan user baru dengan profile dalam satu request")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequestDto dto) {
        userService.register(dto);
        return ResponseEntity.ok("Registrasi berhasil, silakan login.");
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Login user dan mendapatkan JWT token")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto dto) {
        LoginResponseDto response = userService.login(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Logout user dan invalidate token")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        // In a real implementation, you would add the token to a blacklist
        // For now, we'll just return a success message
        return ResponseEntity.ok("Logout berhasil.");
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Refresh JWT token yang sudah ada")
    public ResponseEntity<LoginResponseDto> refresh(@RequestHeader("Authorization") String token) {
        LoginResponseDto response = userService.refreshToken(token);
        return ResponseEntity.ok(response);
    }
}
