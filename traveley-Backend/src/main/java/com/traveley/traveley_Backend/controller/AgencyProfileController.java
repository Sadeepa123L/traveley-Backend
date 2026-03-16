package com.traveley.traveley_Backend.controller;

import com.traveley.traveley_Backend.dto.AgencyProfileDTO;
import com.traveley.traveley_Backend.service.custom.AgencyProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.security.Principal;

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
    }

