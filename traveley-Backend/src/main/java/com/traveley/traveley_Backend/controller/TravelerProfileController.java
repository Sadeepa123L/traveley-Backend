package com.traveley.traveley_Backend.controller;


import com.traveley.traveley_Backend.dto.TravelerProfileDTO;
import com.traveley.traveley_Backend.service.custom.TravelerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/TravelerProfile")
@RequiredArgsConstructor
public class TravelerProfileController {

    private final TravelerProfileService travelerProfileService;

    @PostMapping("/save")
    public ResponseEntity<?> saveProfile(@RequestBody TravelerProfileDTO dto, Principal principal) {
        try {
            travelerProfileService.saveTravelerProfile(dto, principal.getName());
            return ResponseEntity.ok().body("{\"message\": \"Agency Profile saved successfully!\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/getProfile")
    public ResponseEntity<?> getProfile(Principal principal) {
        try {
            TravelerProfileDTO travelerProfileDTO = travelerProfileService.getProfile(principal.getName());
            return ResponseEntity.ok(travelerProfileDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/getAllProfiles")
    public ResponseEntity<List<TravelerProfileDTO>> getAllProfiles() {
        try {
            List<TravelerProfileDTO> allTravelers = travelerProfileService.getAllProfiles();
            return ResponseEntity.ok(allTravelers);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
