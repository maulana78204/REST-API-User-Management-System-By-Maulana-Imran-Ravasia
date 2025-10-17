package com.example.usermanagement.integration;

import com.example.usermanagement.dto.LoginRequestDto;
import com.example.usermanagement.dto.ProfileUpdateDto;
import com.example.usermanagement.dto.RegisterRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProfileControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String registerAndLoginAndGetToken() throws Exception {
        RegisterRequestDto registerDto = new RegisterRequestDto();
        registerDto.setEmail("profile@example.com");
        registerDto.setPassword("password123");
        registerDto.setFullName("Profile User");
        registerDto.setPhone("08123456780");
        registerDto.setAddress("Some Address");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());

        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setEmail("profile@example.com");
        loginDto.setPassword("password123");

        String response = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        com.fasterxml.jackson.databind.JsonNode json = objectMapper.readTree(response);
        return json.get("token").asText();
    }

    @Test
    public void testGetMyProfile_Success() throws Exception {
        String token = registerAndLoginAndGetToken();

        mockMvc.perform(get("/profiles/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("profile@example.com"))
                .andExpect(jsonPath("$.fullName").value("Profile User"));
    }

    @Test
    public void testUpdateMyProfile_Success() throws Exception {
        String token = registerAndLoginAndGetToken();

        ProfileUpdateDto updateDto = new ProfileUpdateDto();
        updateDto.setFullName("Updated Name");
        updateDto.setPhone("0811111111");
        updateDto.setAddress("Updated Address");
        updateDto.setAvatar("updated.jpg");

        mockMvc.perform(put("/profiles/me")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Updated Name"))
                .andExpect(jsonPath("$.address").value("Updated Address"));
    }

    @Test
    public void testDeleteMyProfile_Success() throws Exception {
        String token = registerAndLoginAndGetToken();

        mockMvc.perform(delete("/profiles/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Profile berhasil dihapus."));
    }
}


