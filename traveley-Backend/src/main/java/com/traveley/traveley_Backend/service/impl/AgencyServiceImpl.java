package com.traveley.traveley_Backend.service.impl;

import com.traveley.traveley_Backend.dto.APIResponseDTO;
import com.traveley.traveley_Backend.dto.AgencyResponseDTO;
import com.traveley.traveley_Backend.entity.AgencyProfile;
import com.traveley.traveley_Backend.entity.Role;
import com.traveley.traveley_Backend.entity.User;
import com.traveley.traveley_Backend.repository.AgencyProfileRepo;
import com.traveley.traveley_Backend.repository.AgencyRepo;
import com.traveley.traveley_Backend.repository.UserRepo;
import com.traveley.traveley_Backend.service.custom.AgencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class AgencyServiceImpl implements AgencyService {

    private final AgencyRepo agencyRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AgencyProfileRepo agencyProfileRepo;

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

    @Override
    public String approveAgency(Long agencyId) {
        User agency = agencyRepo.findById(agencyId).orElseThrow(() -> new RuntimeException("Agency not found"));
        agency.setStatus("ACTIVE");
        agencyRepo.save(agency);
        return "Agency approved successfully!";
    }

    @Override
    public void updatePassword(String username, String currentPassword, String newPassword) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    @Override
    public void updateStatus(Long id) {
        AgencyProfile profile = agencyProfileRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Agency not found"));

        User user = profile.getUser();

        if(user != null){
            if("ACTIVE" .equalsIgnoreCase(user.getStatus())){
                user.setStatus("SUSPENDED");
            } else if ("SUSPENDED" .equalsIgnoreCase(user.getStatus())) {
                user.setStatus("ACTIVE");
            }
            userRepo.save(user);
        }
    }
}
