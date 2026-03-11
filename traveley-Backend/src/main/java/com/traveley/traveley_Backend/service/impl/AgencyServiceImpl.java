package com.traveley.traveley_Backend.service.impl;

import com.traveley.traveley_Backend.dto.APIResponseDTO;
import com.traveley.traveley_Backend.dto.AgencyResponseDTO;
import com.traveley.traveley_Backend.entity.Role;
import com.traveley.traveley_Backend.entity.User;
import com.traveley.traveley_Backend.repository.AgencyRepo;
import com.traveley.traveley_Backend.service.custom.AgencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class AgencyServiceImpl implements AgencyService {

    private final AgencyRepo agencyRepo;

    @Override
    public List<AgencyResponseDTO> getPendingAgencies() {
        List<User> pendingAgencies = agencyRepo.findByRoleAndStatus(Role.AGENCY, "PENDING");

        return pendingAgencies.stream().map(user -> {

            String registerDate = (user.getCreatedAt() != null) ?
                    user.getCreatedAt().toLocalDate().toString()
                    : "N/A";

            return new AgencyResponseDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getStatus(),
                    registerDate
            );
        }).collect(Collectors.toList());
    }
}
