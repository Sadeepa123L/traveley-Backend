package com.traveley.traveley_Backend.controller;

import com.traveley.traveley_Backend.dto.TourPackageDTO;
import com.traveley.traveley_Backend.service.custom.TourPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tourPackage")
public class TourPackageController {

    private final TourPackageService tourPackageService;

    @PostMapping(value = "/save", consumes = {"multipart/form-data"})
    public ResponseEntity<?> saveTourPackage(
            Principal principal,
            @RequestPart("packageData")TourPackageDTO tourPackageDTO,
            @RequestPart(value = "image", required = false) MultipartFile image
            ){
        try{
            tourPackageService.addTourPackage(principal.getName(), tourPackageDTO, image);
            return ResponseEntity.ok(Map.of("message", "Tour Package saved successfully!"));
        }catch (IOException e){
            return ResponseEntity.status(500).body(Map.of("error", "Image upload failed: " + e.getMessage()));
        }catch (RuntimeException e){
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error occurred."));
        }
    }

    @GetMapping("/myPackages")
    public ResponseEntity<List<TourPackageDTO>> getMyPackages(Principal principal){
        try{
            List<TourPackageDTO> packages = tourPackageService.getPackagesForCurrentAgency(principal.getName());
            return ResponseEntity.ok(packages);
        }catch (Exception e){
            return ResponseEntity.status(500).body(null);
        }
    }
}
