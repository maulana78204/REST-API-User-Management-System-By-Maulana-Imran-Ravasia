package com.example.usermanagement.service;

import com.example.usermanagement.dto.LoginRequestDto;
import com.example.usermanagement.dto.LoginResponseDto;
import com.example.usermanagement.dto.RegisterRequestDto;
import com.example.usermanagement.entity.*;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.exception.EmailAlreadyUsedException;
import com.example.usermanagement.exception.InvalidCredentialsException;
import com.example.usermanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public void register(RegisterRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyUsedException("Email sudah dipakai.");
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        // Use role from request, default to CUSTOMER if null
        user.setRole(dto.getRole() != null ? dto.getRole() : RoleEnum.CUSTOMER);
        user.setStatus(StatusEnum.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        UserProfile profile = new UserProfile();
        profile.setFullName(dto.getFullName());
        profile.setPhone(dto.getPhone());
        profile.setAddress(dto.getAddress());
        profile.setAvatar(dto.getAvatar());
        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());
        profile.setUser(user);
        user.setUserProfile(profile);

        userRepository.save(user); // Profile ikut ter-save (CascadeType.ALL)
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        // Cari user berdasarkan email
        Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());
        if (userOpt.isEmpty()) {
            throw new InvalidCredentialsException("Email atau password salah.");
        }

        User user = userOpt.get();
        
        // Cek status user
        if (user.getStatus() != StatusEnum.ACTIVE) {
            throw new InvalidCredentialsException("Akun tidak aktif.");
        }

        // Verifikasi password
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Email atau password salah.");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());

        // Ambil data profile
        UserProfile profile = user.getUserProfile();
        String fullName = profile != null ? profile.getFullName() : "";

        // Return response
        return new LoginResponseDto(
            token,
            user.getId(),
            user.getEmail(),
            fullName,
            user.getRole(),
            user.getStatus()
        );
    }

    public LoginResponseDto refreshToken(String token) {
        // Extract token from "Bearer <token>" format
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        // Validate token and extract email
        String email = jwtUtil.extractUsername(token);
        if (email == null) {
            throw new InvalidCredentialsException("Token tidak valid.");
        }
        
        // Find user
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new InvalidCredentialsException("User tidak ditemukan.");
        }
        
        User user = userOpt.get();
        
        // Check if user is still active
        if (user.getStatus() != StatusEnum.ACTIVE) {
            throw new InvalidCredentialsException("Akun tidak aktif.");
        }
        
        // Generate new token
        String newToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        
        // Get profile data
        UserProfile profile = user.getUserProfile();
        String fullName = profile != null ? profile.getFullName() : "";
        
        // Return new token response
        return new LoginResponseDto(
            newToken,
            user.getId(),
            user.getEmail(),
            fullName,
            user.getRole(),
            user.getStatus()
        );
    }
}
