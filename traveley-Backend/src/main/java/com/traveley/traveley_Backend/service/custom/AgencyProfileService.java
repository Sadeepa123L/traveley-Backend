package com.traveley.traveley_Backend.service.custom;

import com.traveley.traveley_Backend.dto.AgencyProfileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AgencyProfileService {
    void saveOrUpdateProfile(String username, AgencyProfileDTO agencyProfileDTO, MultipartFile profileImage) throws IOException;
    AgencyProfileDTO getProfile(String username);
}
