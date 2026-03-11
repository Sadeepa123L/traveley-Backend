package com.traveley.traveley_Backend.service.custom;

import com.traveley.traveley_Backend.dto.APIResponseDTO;
import com.traveley.traveley_Backend.dto.AgencyResponseDTO;

import java.util.List;

public interface AgencyService {
    List<AgencyResponseDTO> getPendingAgencies();
}
