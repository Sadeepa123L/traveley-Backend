package com.traveley.traveley_Backend.repository;

import com.traveley.traveley_Backend.entity.TourPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TourPackageRepo extends JpaRepository<TourPackage, Long> {
}
