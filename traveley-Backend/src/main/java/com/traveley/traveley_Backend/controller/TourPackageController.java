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

    @PutMapping(value = "/update/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateTourPackage(
            @PathVariable Long id,
            Principal principal,
            @RequestPart("packageData") TourPackageDTO tourPackageDTO,
            @RequestPart(value = "image", required = false) MultipartFile image
    ){
        try{
            TourPackageDTO updatePackage = tourPackageService.updateTourPackage(id, principal.getName(), tourPackageDTO, image);
            return ResponseEntity.ok(Map.of(
                    "message", "Tour Package updated successfully! 🚀",
                    "data", updatePackage
            ));
        }catch (IOException e){
            return ResponseEntity.status(500).body(Map.of("error", "Image upload failed: " + e.getMessage()));
        }catch (RuntimeException e){
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error occurred."));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTourPackage(@PathVariable Long id, Principal principal){
        try{
            tourPackageService.deletePackage(id, principal.getName());
            return ResponseEntity.ok(Map.of("message", "Tour Package deleted successfully! 🗑️"));
        }catch (RuntimeException e){
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }
    @GetMapping("/activePackages")
    public ResponseEntity<?> getActivePackages(){
        try {
            List<TourPackageDTO> packages = tourPackageService.getAllPackages();
            return ResponseEntity.ok(packages);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
