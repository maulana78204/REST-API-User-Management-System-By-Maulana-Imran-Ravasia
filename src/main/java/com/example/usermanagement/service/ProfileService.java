package com.example.usermanagement.service;

import com.example.usermanagement.dto.ProfileResponseDto;
import com.example.usermanagement.dto.ProfileUpdateDto;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.entity.UserProfile;
import com.example.usermanagement.exception.ResourceNotFoundException;
import com.example.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    public ProfileResponseDto getProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan dengan email: " + email));
        
        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            throw new ResourceNotFoundException("Profile tidak ditemukan untuk user: " + email);
        }
        
        return new ProfileResponseDto(
                user.getId().toString(),
                user.getEmail(),
                profile.getFullName(),
                profile.getPhone(),
                profile.getAddress(),
                profile.getAvatar(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Transactional
    public ProfileResponseDto updateProfile(String email, ProfileUpdateDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan dengan email: " + email));
        
        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            throw new ResourceNotFoundException("Profile tidak ditemukan untuk user: " + email);
        }
        
        // Apply partial updates only for provided fields
        if (dto.getFullName() != null) {
            profile.setFullName(dto.getFullName());
        }
        if (dto.getPhone() != null) {
            profile.setPhone(dto.getPhone());
        }
        if (dto.getAddress() != null) {
            profile.setAddress(dto.getAddress());
        }
        if (dto.getAvatar() != null) {
            profile.setAvatar(dto.getAvatar());
        }
        profile.setUpdatedAt(LocalDateTime.now());
        
        // Update user timestamp
        user.setUpdatedAt(LocalDateTime.now());
        
        // Save changes
        userRepository.save(user);
        
        return new ProfileResponseDto(
                user.getId().toString(),
                user.getEmail(),
                profile.getFullName(),
                profile.getPhone(),
                profile.getAddress(),
                profile.getAvatar(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Transactional
    public void deleteProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan dengan email: " + email));
        
        // Delete user (profile will be deleted due to CascadeType.ALL)
        userRepository.delete(user);
    }
}