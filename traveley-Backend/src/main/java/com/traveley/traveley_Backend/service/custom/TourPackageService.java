package com.traveley.traveley_Backend.service.custom;

import com.traveley.traveley_Backend.dto.TourPackageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TourPackageService {
    void addTourPackage(String username, TourPackageDTO tourPackageDTO, MultipartFile photo) throws IOException;
    List<TourPackageDTO> getPackagesForCurrentAgency(String username);
    TourPackageDTO updateTourPackage(Long id, String username, TourPackageDTO tourPackageDTO, MultipartFile photo) throws IOException;
    void deletePackage(Long id, String username);
    List<TourPackageDTO> getAllPackages();
}
