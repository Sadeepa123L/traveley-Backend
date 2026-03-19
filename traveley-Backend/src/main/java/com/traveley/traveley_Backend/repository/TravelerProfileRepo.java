package com.traveley.traveley_Backend.repository;

import com.traveley.traveley_Backend.entity.TravelerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TravelerProfileRepo extends JpaRepository<TravelerProfile, Long> {
   Optional<TravelerProfile> findByUser_Id(Long id);
}
