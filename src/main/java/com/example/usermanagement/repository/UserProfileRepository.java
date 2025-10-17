package com.example.usermanagement.repository;

import com.example.usermanagement.entity.UserProfile;
import com.example.usermanagement.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findByUser(User user);
    Optional<UserProfile> findByUserId(UUID userId);
}
