package com.traveley.traveley_Backend.controller;

import com.traveley.traveley_Backend.dto.AgencyProfileDTO;
import com.traveley.traveley_Backend.dto.AgencyResponseDTO;
import com.traveley.traveley_Backend.service.custom.AgencyProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/agencyProfile")
@RequiredArgsConstructor
public class AgencyProfileController {

    private final AgencyProfileService agencyProfileService;

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveProfile(
            @RequestPart("agencyData")AgencyProfileDTO agencyProfileDTO,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            Principal principal
            ){
        try {
            agencyProfileService.saveOrUpdateProfile(principal.getName(), agencyProfileDTO, photo);
            return ResponseEntity.ok().body("{\"message\": \"Agency Profile saved successfully!\"}");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    @GetMapping("/getProfile")
    public ResponseEntity<?> getProfile(Principal principal){
        try{
            AgencyProfileDTO agencyProfileDTO = agencyProfileService.getProfile(principal.getName());
            return ResponseEntity.ok(agencyProfileDTO);
        }catch (RuntimeException e){
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/activeAgencies")
    public ResponseEntity<?> getActiveAgencies(){
        try{
            List<AgencyProfileDTO> agencies = agencyProfileService.getAllActiveAgencies();
            return ResponseEntity.ok(agencies);
        }catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}

