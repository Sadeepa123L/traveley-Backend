package com.traveley.traveley_Backend.service.impl;

import com.traveley.traveley_Backend.dto.TravelerProfileDTO;
import com.traveley.traveley_Backend.entity.TravelerProfile;
import com.traveley.traveley_Backend.entity.User;
import com.traveley.traveley_Backend.repository.TravelerProfileRepo;
import com.traveley.traveley_Backend.repository.UserRepo;
import com.traveley.traveley_Backend.service.custom.TravelerProfileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TravelerProfileImpl implements TravelerProfileService {

    private final UserRepo userRepo;
    private final TravelerProfileRepo travelerProfileRepo;
    private final ModelMapper modelMapper;

    @Override
    public void saveTravelerProfile(TravelerProfileDTO travelerProfileDTO, String username) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long userId = user.getId();

        TravelerProfile travelerProfile = travelerProfileRepo.findByUser_Id(userId)
                .orElse(new TravelerProfile());

        travelerProfile.setUser(user);

        travelerProfile.setFirstName(travelerProfileDTO.getFirstName());
        travelerProfile.setLastName(travelerProfileDTO.getLastName());
        travelerProfile.setMobileNumber(travelerProfileDTO.getMobileNumber());
        travelerProfile.setCountry(travelerProfileDTO.getCountry());

         travelerProfileRepo.save(travelerProfile);
    }

    @Override
    public TravelerProfileDTO getProfile(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TravelerProfile travelerProfile = travelerProfileRepo.findByUser_Id(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return modelMapper.map(travelerProfile, TravelerProfileDTO.class);
    }

    @Override
    @Transactional
    public void deleteTraveler(Long id) {

        TravelerProfile travelerProfile = travelerProfileRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        User user = travelerProfile.getUser();

        travelerProfileRepo.delete(travelerProfile);

        if (user != null) {
            userRepo.delete(user);
        }
    }

    @Override
    public List<TravelerProfileDTO> getAllProfiles() {
        List<TravelerProfile> profiles = travelerProfileRepo.findAll();

        return profiles.stream().map(profile -> modelMapper
                .map(profile, TravelerProfileDTO.class))
                .collect(Collectors.toList());
    }

}
