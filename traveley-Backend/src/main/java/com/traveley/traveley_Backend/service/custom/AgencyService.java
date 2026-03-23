package com.traveley.traveley_Backend.service.custom;

import com.traveley.traveley_Backend.dto.AgencyResponseDTO;

import java.util.List;

public interface AgencyService {
    List<AgencyResponseDTO> getPendingAgencies();
    String approveAgency(Long Id);
    void updatePassword(String username, String currentPassword, String newPassword);
    void updateStatus(Long id);
    void deleteAgency(Long id);
}
