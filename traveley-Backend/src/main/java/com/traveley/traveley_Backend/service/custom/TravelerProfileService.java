package com.traveley.traveley_Backend.service.custom;

import com.traveley.traveley_Backend.dto.TravelerProfileDTO;

public interface TravelerProfileService {
    void saveTravelerProfile(TravelerProfileDTO travelerProfileDTO, String username);
    TravelerProfileDTO getProfile(String username);
}
