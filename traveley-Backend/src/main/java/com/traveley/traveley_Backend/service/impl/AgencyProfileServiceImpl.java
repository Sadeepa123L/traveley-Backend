package com.traveley.traveley_Backend.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.traveley.traveley_Backend.dto.AgencyProfileDTO;
import com.traveley.traveley_Backend.entity.AgencyProfile;
import com.traveley.traveley_Backend.entity.User;
import com.traveley.traveley_Backend.repository.AgencyProfileRepo;
import com.traveley.traveley_Backend.repository.UserRepo;
import com.traveley.traveley_Backend.service.custom.AgencyProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class AgencyProfileServiceImpl implements AgencyProfileService {

    private final AgencyProfileRepo agencyProfileRepo;
    private final UserRepo userRepo;
    private final Cloudinary cloudinary;


    @Override
    public void saveOrUpdateProfile(String username, AgencyProfileDTO agencyProfileDTO, MultipartFile profileImage) throws IOException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));

        Long userId = user.getId();

        AgencyProfile agencyProfile = agencyProfileRepo.findByUserId(userId).orElse(new AgencyProfile());

        agencyProfile.setUser(user);

        agencyProfile.setAgencyName(agencyProfileDTO.getAgencyName());
        agencyProfile.setRegistrationNumber(agencyProfileDTO.getRegistrationNumber());
        agencyProfile.setContactNumber(agencyProfileDTO.getContactNumber());
        agencyProfile.setAddress(agencyProfileDTO.getAddress());
        agencyProfile.setDescription(agencyProfileDTO.getDescription());

        if (profileImage != null && !profileImage.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(profileImage.getBytes(), ObjectUtils.emptyMap());
            String photoUrl = uploadResult.get("secure_url").toString();
            agencyProfile.setPhotoUrl(photoUrl);
        }

        agencyProfileRepo.save(agencyProfile);
    }
}
