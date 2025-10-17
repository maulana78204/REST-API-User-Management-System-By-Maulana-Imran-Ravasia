package com.example.usermanagement.controller;

import com.example.usermanagement.dto.ProfileResponseDto;
import com.example.usermanagement.dto.ProfileUpdateDto;
import com.example.usermanagement.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/profiles")
@Tag(name = "Profile Management", description = "API untuk mengelola profile user")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/me")
    @Operation(summary = "Get my profile", description = "Mendapatkan profile user yang sedang login")
    public ResponseEntity<ProfileResponseDto> getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        ProfileResponseDto profile = profileService.getProfileByEmail(email);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    @Operation(summary = "Update my profile", description = "Update profile user yang sedang login")
    public ResponseEntity<ProfileResponseDto> updateMyProfile(
            @RequestBody @Valid ProfileUpdateDto dto,
            Authentication authentication) {
        String email = authentication.getName();
        ProfileResponseDto updatedProfile = profileService.updateProfile(email, dto);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/me")
    @Operation(summary = "Delete my profile", description = "Hapus profile user yang sedang login")
    public ResponseEntity<String> deleteMyProfile(Authentication authentication) {
        String email = authentication.getName();
        profileService.deleteProfile(email);
        return ResponseEntity.ok("Profile berhasil dihapus.");
    }
}
