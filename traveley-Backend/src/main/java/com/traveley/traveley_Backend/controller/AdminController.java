package com.traveley.traveley_Backend.controller;

import com.traveley.traveley_Backend.service.custom.TravelerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final TravelerProfileService travelerProfileService;

    @DeleteMapping("/traveler/{id}")
    public ResponseEntity<String> deleteProfile(@PathVariable Long id) {
        travelerProfileService.deleteTraveler(id);
        return ResponseEntity.ok("Deleted traveler");
    }
}
