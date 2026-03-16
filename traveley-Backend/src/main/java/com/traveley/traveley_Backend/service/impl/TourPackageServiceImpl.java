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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TourPackageServiceImpl implements TourPackageService {
    private final UserRepo userRepo;
    private final AgencyProfileRepo agencyProfileRepo;
    private final TourPackageRepo tourPackageRepo;
    private final Cloudinary cloudinary;
    private final ModelMapper modelMapper;


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

    @Override
    public List<TourPackageDTO> getPackagesForCurrentAgency(String username) {

        User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));

        Optional<AgencyProfile> agencyProfileOpt = agencyProfileRepo.findByUserId(user.getId());
        if (agencyProfileOpt.isEmpty()) {
            return new ArrayList<>();
        }

        List<TourPackage> packages = tourPackageRepo.findByAgencyProfile_Id(agencyProfileOpt.get().getId());

        if (packages == null || packages.isEmpty()) {
            return new ArrayList<>();
        }

        return packages.stream()
                .map(pkg -> modelMapper.map(pkg, TourPackageDTO.class))
                .toList();
    }

    @Override
    public TourPackageDTO updateTourPackage(Long id, String username, TourPackageDTO tourPackageDTO, MultipartFile image) throws IOException {

        //user find
        User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));

        //Agency profile find
        AgencyProfile agencyProfile = agencyProfileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Agency Profile not found. Please complete your profile first."));

        //Tour package find
        TourPackage existingPackage = tourPackageRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Tour Package not found with ID:" + id));

        //new details
        existingPackage.setTitle(tourPackageDTO.getTitle());
        existingPackage.setDescription(tourPackageDTO.getDescription());
        existingPackage.setDestination(tourPackageDTO.getDestination());
        existingPackage.setPrice(tourPackageDTO.getPrice());
        existingPackage.setDuration(tourPackageDTO.getDuration());

        //new image update
        if (image != null && !image.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = uploadResult.get("secure_url").toString();
            existingPackage.setImageUrl(imageUrl);
        }

        //update krapu package eka save karala DTO ekak widiyata return karanwa
        TourPackage savedPackage = tourPackageRepo.save(existingPackage);
        return modelMapper.map(savedPackage, TourPackageDTO.class);
    }

    @Override
    public void deletePackage(Long id, String username) {

        User user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));

        TourPackage tourPackage = tourPackageRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Tour Package not found with ID:" + id));

        tourPackageRepo.delete(tourPackage);
    }

}
