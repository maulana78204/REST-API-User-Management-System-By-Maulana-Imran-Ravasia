package com.example.usermanagement.controller;

import com.example.usermanagement.dto.AdminUserResponseDto;
import com.example.usermanagement.dto.AdminUserUpdateDto;
import com.example.usermanagement.dto.StatusUpdateDto;
import com.example.usermanagement.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Management", description = "API untuk admin mengelola semua user")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Mendapatkan daftar semua user dengan pagination dan sorting")
    public ResponseEntity<Page<AdminUserResponseDto>> getAllUsers(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        Page<AdminUserResponseDto> users = adminService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Get user by ID", description = "Mendapatkan detail user tertentu berdasarkan ID")
    public ResponseEntity<AdminUserResponseDto> getUserById(
            @Parameter(description = "User ID") @PathVariable UUID id) {
        AdminUserResponseDto user = adminService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}")
    @Operation(summary = "Update user", description = "Update data user (email, role, dll)")
    public ResponseEntity<AdminUserResponseDto> updateUser(
            @Parameter(description = "User ID") @PathVariable UUID id,
            @RequestBody @Valid AdminUserUpdateDto dto) {
        AdminUserResponseDto updatedUser = adminService.updateUser(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/users/{id}/status")
    @Operation(summary = "Update user status", description = "Ubah status user (ACTIVE ↔ INACTIVE)")
    public ResponseEntity<AdminUserResponseDto> updateUserStatus(
            @Parameter(description = "User ID") @PathVariable UUID id,
            @RequestBody @Valid StatusUpdateDto dto) {
        AdminUserResponseDto updatedUser = adminService.updateUserStatus(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete user", description = "Hapus user beserta profile")
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "User ID") @PathVariable UUID id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok("User berhasil dihapus.");
    }
}
