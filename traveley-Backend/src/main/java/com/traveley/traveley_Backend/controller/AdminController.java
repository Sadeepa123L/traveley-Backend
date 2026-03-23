package com.traveley.traveley_Backend.controller;

import com.traveley.traveley_Backend.service.custom.AgencyService;
import com.traveley.traveley_Backend.service.custom.TravelerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final TravelerProfileService travelerProfileService;
    private final AgencyService agencyService;

    @DeleteMapping("/traveler/{id}")
    public ResponseEntity<String> deleteProfile(@PathVariable Long id) {
        travelerProfileService.deleteTraveler(id);
        return ResponseEntity.ok("Deleted traveler");
    }
    @DeleteMapping("/deleteAgency/{id}")
    public ResponseEntity<?> deleteAgency(@PathVariable Long id){
        try {
            agencyService.deleteAgency(id);
            return ResponseEntity.ok("Deleted agency");
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}
