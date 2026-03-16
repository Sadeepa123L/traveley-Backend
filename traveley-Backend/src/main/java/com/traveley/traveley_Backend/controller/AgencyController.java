package com.traveley.traveley_Backend.controller;

import com.traveley.traveley_Backend.dto.APIResponseDTO;
import com.traveley.traveley_Backend.dto.AgencyResponseDTO;
import com.traveley.traveley_Backend.service.custom.AgencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("/approve/{id}")
    public ResponseEntity<APIResponseDTO> approveAgency(@PathVariable Long id){
        String result = agencyService.approveAgency(id);

        APIResponseDTO response = new APIResponseDTO(
                200,
                result,
                null
        );
        return  ResponseEntity.ok(response);
    }
}
