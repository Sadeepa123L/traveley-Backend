package com.traveley.traveley_Backend.repository;

import com.traveley.traveley_Backend.entity.TourPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TourPackageRepo extends JpaRepository<TourPackage, Long> {
    List<TourPackage> findByAgencyProfile_Id(Long agencyProfileId);
}
