package com.example.usermanagement.repository;

import com.example.usermanagement.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import com.example.usermanagement.dto.AdminUserListViewDto;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query(value = "SELECT CAST(u.id AS varchar) as id, u.email as email, p.full_name as fullName, p.phone as phone, p.address as address, p.avatar as avatar, CAST(u.role as varchar) as role, CAST(u.status as varchar) as status, u.created_at as createdAt, u.updated_at as updatedAt FROM users u JOIN user_profiles p ON p.user_id = u.id", countQuery = "SELECT count(*) FROM users u JOIN user_profiles p ON p.user_id = u.id", nativeQuery = true)
    Page<AdminUserListViewDto> findAllUsersWithProfile(Pageable pageable);
}
