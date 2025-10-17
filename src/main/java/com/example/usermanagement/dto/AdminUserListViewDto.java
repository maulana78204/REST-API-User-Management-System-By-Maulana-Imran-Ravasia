package com.example.usermanagement.dto;

import java.time.LocalDateTime;

// Projection interface for native query mapping
public interface AdminUserListViewDto {
    String getId();
    String getEmail();
    String getFullName();
    String getPhone();
    String getAddress();
    String getAvatar();
    String getRole();
    String getStatus();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}


