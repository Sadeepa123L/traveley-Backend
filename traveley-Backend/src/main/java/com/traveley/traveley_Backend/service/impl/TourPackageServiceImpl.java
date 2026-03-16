package com.traveley.traveley_Backend.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.traveley.traveley_Backend.dto.TourPackageDTO;
import com.traveley.traveley_Backend.entity.AgencyProfile;
import com.traveley.traveley_Backend.entity.TourPackage;
import com.traveley.traveley_Backend.entity.User;
import com.traveley.traveley_Backend.repository.AgencyProfileRepo;
import com.traveley.traveley_Backend.repository.TourPackageRepo;
import com.traveley.traveley_Backend.repository.UserRepo;
import com.traveley.traveley_Backend.service.custom.TourPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class TourPackageServiceImpl implements TourPackageService {
    private final UserRepo userRepo;
    private final AgencyProfileRepo agencyProfileRepo;
    private final TourPackageRepo tourPackageRepo;
    private final Cloudinary cloudinary;


    @Override
    public void addTourPackage(String username, TourPackageDTO tourPackageDTO, MultipartFile photo) throws IOException {
        User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));

        AgencyProfile agencyProfile = agencyProfileRepo.findByUserId(user.getId())
                .orElseThrow(()-> new RuntimeException("Agency Profile not found. Please complete your profile first."));

        TourPackage tourPackage = new TourPackage();

        tourPackage.setTitle(tourPackageDTO.getTitle());
        tourPackage.setDescription(tourPackageDTO.getDescription());
        tourPackage.setDestination(tourPackageDTO.getDestination());
        tourPackage.setPrice(tourPackageDTO.getPrice());
        tourPackage.setDuration(tourPackageDTO.getDuration());

        tourPackage.setAgencyProfile(agencyProfile);

        if (photo != null && !photo.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(photo.getBytes(), ObjectUtils.emptyMap());
            String photoUrl = uploadResult.get("secure_url").toString();

            tourPackage.setImageUrl(photoUrl);
        }
        tourPackageRepo.save(tourPackage);
    }
}
