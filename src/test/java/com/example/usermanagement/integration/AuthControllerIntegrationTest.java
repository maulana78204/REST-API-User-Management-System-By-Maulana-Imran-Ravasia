package com.example.usermanagement.integration;

import com.example.usermanagement.dto.LoginRequestDto;
import com.example.usermanagement.dto.RegisterRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterUser_Success() throws Exception {
        RegisterRequestDto registerDto = new RegisterRequestDto();
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("password123");
        registerDto.setFullName("Test User");
        registerDto.setPhone("08123456789");
        registerDto.setAddress("Test Address");
        registerDto.setAvatar("test-avatar.jpg");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Registrasi berhasil, silakan login."));
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() throws Exception {
        // First registration
        RegisterRequestDto registerDto = new RegisterRequestDto();
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("password123");
        registerDto.setFullName("Test User");
        registerDto.setPhone("08123456789");
        registerDto.setAddress("Test Address");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());

        // Second registration with same email
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email sudah dipakai."));
    }

    @Test
    public void testLoginUser_Success() throws Exception {
        // First register a user
        RegisterRequestDto registerDto = new RegisterRequestDto();
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("password123");
        registerDto.setFullName("Test User");
        registerDto.setPhone("08123456789");
        registerDto.setAddress("Test Address");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());

        // Then login
        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password123");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.fullName").value("Test User"));
    }

    @Test
    public void testLoginUser_InvalidCredentials() throws Exception {
        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setEmail("nonexistent@example.com");
        loginDto.setPassword("wrongpassword");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Email atau password salah."));
    }
}
