package com.traveley.traveley_Backend.repository;

import com.traveley.traveley_Backend.entity.AgencyProfile;
import com.traveley.traveley_Backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgencyProfileRepo extends JpaRepository<AgencyProfile, Long> {
    Optional<AgencyProfile> findByUserId(Long id);
    List<AgencyProfile> findByUser_StatusAndUser_Role(String user_status, Role user_role);
}
