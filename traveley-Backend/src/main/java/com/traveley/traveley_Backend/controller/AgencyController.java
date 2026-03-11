package com.traveley.traveley_Backend.controller;

import com.traveley.traveley_Backend.dto.APIResponseDTO;
import com.traveley.traveley_Backend.dto.AgencyResponseDTO;
import com.traveley.traveley_Backend.service.custom.AgencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agency")
@RequiredArgsConstructor
public class AgencyController {

    private final AgencyService agencyService;

    @GetMapping("/pending")
    public ResponseEntity<APIResponseDTO> getPendingAgencies(){
        List<AgencyResponseDTO> pendingAgencies = agencyService.getPendingAgencies();

        APIResponseDTO response = new APIResponseDTO(
                200,
                "Pending agencies fetched successfully",
                pendingAgencies
        );
        return  ResponseEntity.ok(response);
    }
}
