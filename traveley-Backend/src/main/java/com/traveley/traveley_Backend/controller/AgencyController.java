package com.traveley.traveley_Backend.controller;

import com.traveley.traveley_Backend.dto.APIResponseDTO;
import com.traveley.traveley_Backend.dto.AgencyResponseDTO;
import com.traveley.traveley_Backend.dto.PasswordUpdateDTO;
import com.traveley.traveley_Backend.service.custom.AgencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

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
    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordUpdateDTO passwordUpdateDTO, Principal principal){
        try{
            agencyService.updatePassword(principal.getName(), passwordUpdateDTO.getCurrentPassword(), passwordUpdateDTO.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "Password updated successfully!"));
        }catch (RuntimeException e){
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }
    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id){
        try {
            agencyService.updateStatus(id);
            return ResponseEntity.ok(Map.of("message", "Status updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
