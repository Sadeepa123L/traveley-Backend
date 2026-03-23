package com.traveley.traveley_Backend.service.custom;

import com.traveley.traveley_Backend.dto.AgencyProfileDTO;
import com.traveley.traveley_Backend.dto.AgencyProfileResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AgencyProfileService {
    void saveOrUpdateProfile(String username, AgencyProfileDTO agencyProfileDTO, MultipartFile profileImage) throws IOException;
    AgencyProfileDTO getProfile(String username);
    List<AgencyProfileDTO> getAllActiveAgencies();
    List<AgencyProfileResponseDTO> getAllActiveAndSuspendAgencies();
}
