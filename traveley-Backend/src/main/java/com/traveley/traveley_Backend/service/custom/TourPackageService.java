package com.traveley.traveley_Backend.service.custom;

import com.traveley.traveley_Backend.dto.TourPackageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TourPackageService {
    void addTourPackage(String username, TourPackageDTO tourPackageDTO, MultipartFile photo) throws IOException;
}
