package com.traveley.traveley_Backend.repository;

import com.traveley.traveley_Backend.entity.AgencyProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgencyProfileRepo extends JpaRepository<AgencyProfile, Long> {
    Optional<AgencyProfile> findByUserId(Long id);
}
