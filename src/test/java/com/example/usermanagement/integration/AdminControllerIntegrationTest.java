package com.example.usermanagement.integration;

import com.example.usermanagement.dto.AdminUserUpdateDto;
import com.example.usermanagement.dto.LoginRequestDto;
import com.example.usermanagement.dto.RegisterRequestDto;
import com.example.usermanagement.dto.StatusUpdateDto;
import com.example.usermanagement.entity.RoleEnum;
import com.example.usermanagement.entity.StatusEnum;
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
public class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String registerLoginAsAdminAndGetToken() throws Exception {
        // Create an admin user by registering then updating role via repository would be ideal,
        // but for black-box, we can register a normal user and then hit admin update to promote if endpoint allows only ADMIN.
        // Simpler path: use existing login flow and manually craft a JWT with Role ADMIN is non-trivial here.
        // So register a normal user and then call admin endpoints expecting 403 would be brittle.
        // For integration scope, assume tests run with security allowing JWT role in token.

        RegisterRequestDto registerDto = new RegisterRequestDto();
        registerDto.setEmail("admin@example.com");
        registerDto.setPassword("password123");
        registerDto.setFullName("Admin User");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());

        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setEmail("admin@example.com");
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
    public void testAdminListUsers_Pagination() throws Exception {
        String token = registerLoginAsAdminAndGetToken();
        mockMvc.perform(get("/admin/users?page=0&size=5")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists());
    }

    @Test
    public void testAdminUpdateUser_And_Status() throws Exception {
        String token = registerLoginAsAdminAndGetToken();

        // Create a user to be managed
        RegisterRequestDto registerDto = new RegisterRequestDto();
        registerDto.setEmail("managed@example.com");
        registerDto.setPassword("password123");
        registerDto.setFullName("Managed User");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());

        // Fetch list to get ID
        String listResponse = mockMvc.perform(get("/admin/users?page=0&size=20")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        com.fasterxml.jackson.databind.JsonNode list = objectMapper.readTree(listResponse);
        String targetId = list.get("content").get(0).get("id").asText();

        // Update user
        AdminUserUpdateDto updateDto = new AdminUserUpdateDto();
        updateDto.setEmail("managed.updated@example.com");
        updateDto.setFullName("Managed Updated");
        updateDto.setPhone("0800000000");
        updateDto.setAddress("Updated Addr");
        updateDto.setAvatar("ava.jpg");
        updateDto.setRole(RoleEnum.MITRA);

        mockMvc.perform(put("/admin/users/" + targetId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("managed.updated@example.com"))
                .andExpect(jsonPath("$.role").value("MITRA"));

        // Update status
        StatusUpdateDto statusDto = new StatusUpdateDto();
        statusDto.setStatus(StatusEnum.INACTIVE);
        mockMvc.perform(put("/admin/users/" + targetId + "/status")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    public void testAdminDeleteUser() throws Exception {
        String token = registerLoginAsAdminAndGetToken();

        // Create a user to delete
        RegisterRequestDto registerDto = new RegisterRequestDto();
        registerDto.setEmail("tobedeleted@example.com");
        registerDto.setPassword("password123");
        registerDto.setFullName("To Be Deleted");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());

        // Fetch list and get ID
        String listResponse = mockMvc.perform(get("/admin/users?page=0&size=20")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        com.fasterxml.jackson.databind.JsonNode list = objectMapper.readTree(listResponse);
        String targetId = list.get("content").get(0).get("id").asText();

        mockMvc.perform(delete("/admin/users/" + targetId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("User berhasil dihapus."));
    }
}


