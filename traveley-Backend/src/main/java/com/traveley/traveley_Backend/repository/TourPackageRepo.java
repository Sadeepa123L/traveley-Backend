package com.traveley.traveley_Backend.repository;

import com.traveley.traveley_Backend.entity.TourPackage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TourPackageRepo extends JpaRepository<TourPackage, Long> {
    List<TourPackage> findByAgencyProfile_Id(Long agencyProfileId);

    @Query("SELECT t FROM TourPackage t WHERE t.agencyProfile.user.status = :status")
    List<TourPackage> findActivePackages(@Param("status") String status);

    @Query("SELECT t FROM TourPackage t WHERE t.agencyProfile.user.status = :status ORDER BY t.id DESC")
    List<TourPackage> findLatestPackages(@Param("status") String status, Pageable limit);
}
