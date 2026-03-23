package com.traveley.traveley_Backend.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.traveley.traveley_Backend.dto.AgencyProfileDTO;
import com.traveley.traveley_Backend.dto.AgencyProfileResponseDTO;
import com.traveley.traveley_Backend.entity.AgencyProfile;
import com.traveley.traveley_Backend.entity.Role;
import com.traveley.traveley_Backend.entity.User;
import com.traveley.traveley_Backend.repository.AgencyProfileRepo;
import com.traveley.traveley_Backend.repository.UserRepo;
import com.traveley.traveley_Backend.service.custom.AgencyProfileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class AgencyProfileServiceImpl implements AgencyProfileService {

    private final AgencyProfileRepo agencyProfileRepo;
    private final UserRepo userRepo;
    private final Cloudinary cloudinary;
    private final ModelMapper modelMapper;


    @Override
    public void saveOrUpdateProfile(String username, AgencyProfileDTO agencyProfileDTO, MultipartFile profileImage) throws IOException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

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

    @Override
    public AgencyProfileDTO getProfile(String username) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AgencyProfile agencyProfile = agencyProfileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Agency profile not found"));

        return modelMapper.map(agencyProfile, AgencyProfileDTO.class);
    }

    @Override
    public List<AgencyProfileDTO> getAllActiveAgencies() {
        List<AgencyProfile> activeAgencies = agencyProfileRepo.findByUser_StatusAndUser_Role("ACTIVE", Role.AGENCY);

        return activeAgencies.stream()
                .map(profile -> modelMapper.map(profile, AgencyProfileDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AgencyProfileResponseDTO> getAllActiveAndSuspendAgencies() {

        List<String> statuses = Arrays.asList("ACTIVE", "SUSPENDED");
        List<AgencyProfile> agencies = agencyProfileRepo.findByUser_StatusInAndUser_Role(statuses, Role.AGENCY);

        return agencies.stream()
                .map(agency -> {
                    AgencyProfileResponseDTO agencyProfileResponseDTO = modelMapper.map(agency, AgencyProfileResponseDTO.class);

                    agency.getUser().setStatus(agency.getUser().getStatus());

                    if (agency.getUser() != null) {
                        agencyProfileResponseDTO.setStatus(agency.getUser().getStatus());
                    }
                    return agencyProfileResponseDTO;
                })
                .collect(Collectors.toList());
    }
}
