package com.traveley.traveley_Backend.repository;

import com.traveley.traveley_Backend.entity.Role;
import com.traveley.traveley_Backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgencyRepo extends JpaRepository<User, Long> {
    List<User> findByRoleAndStatus(Role role, String status);
}
