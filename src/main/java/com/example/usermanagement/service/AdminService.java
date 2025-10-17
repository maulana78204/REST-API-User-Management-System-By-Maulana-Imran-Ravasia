package com.example.usermanagement.service;

import com.example.usermanagement.dto.AdminUserResponseDto;
import com.example.usermanagement.dto.AdminUserUpdateDto;
import com.example.usermanagement.dto.StatusUpdateDto;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.entity.UserProfile;
import com.example.usermanagement.exception.EmailAlreadyUsedException;
import com.example.usermanagement.exception.ResourceNotFoundException;
import com.example.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public Page<AdminUserResponseDto> getAllUsers(Pageable pageable) {
        Page<com.example.usermanagement.dto.AdminUserListViewDto> page = userRepository.findAllUsersWithProfile(pageable);
        return page.map(v -> new AdminUserResponseDto(
                v.getId(),
                v.getEmail(),
                v.getFullName(),
                v.getPhone(),
                v.getAddress(),
                v.getAvatar(),
                v.getRole(),
                v.getStatus(),
                v.getCreatedAt(),
                v.getUpdatedAt()
        ));
    }

    public AdminUserResponseDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan dengan ID: " + id));
        return convertToAdminUserResponseDto(user);
    }

    @Transactional
    public AdminUserResponseDto updateUser(UUID id, AdminUserUpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan dengan ID: " + id));
        
        // Check if email is already used by another user
        if (!user.getEmail().equals(dto.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new EmailAlreadyUsedException("Email sudah digunakan oleh user lain.");
            }
        }
        
        // Update user data
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setUpdatedAt(LocalDateTime.now());
        
        // Update profile data
        UserProfile profile = user.getUserProfile();
        if (profile != null) {
            profile.setFullName(dto.getFullName());
            profile.setPhone(dto.getPhone());
            profile.setAddress(dto.getAddress());
            profile.setAvatar(dto.getAvatar());
            profile.setUpdatedAt(LocalDateTime.now());
        }
        
        // Save changes
        userRepository.save(user);
        
        return convertToAdminUserResponseDto(user);
    }

    @Transactional
    public AdminUserResponseDto updateUserStatus(UUID id, StatusUpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan dengan ID: " + id));
        
        user.setStatus(dto.getStatus());
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
        
        return convertToAdminUserResponseDto(user);
    }

    @Transactional
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan dengan ID: " + id));
        
        userRepository.delete(user);
    }

    private AdminUserResponseDto convertToAdminUserResponseDto(User user) {
        UserProfile profile = user.getUserProfile();
        String fullName = profile != null ? profile.getFullName() : "";
        String phone = profile != null ? profile.getPhone() : "";
        String address = profile != null ? profile.getAddress() : "";
        String avatar = profile != null ? profile.getAvatar() : "";
        return new AdminUserResponseDto(
            user.getRole(),
            user.getStatus(),
            user.getId().toString(),
            user.getEmail(),
            fullName,
            phone,
            address,
            avatar,
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}