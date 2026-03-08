package com.traveley.traveley_Backend.controller;

import com.traveley.traveley_Backend.dto.APIResponseDTO;
import com.traveley.traveley_Backend.dto.AuthDTO;
import com.traveley.traveley_Backend.dto.RegisterDTO;
import com.traveley.traveley_Backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/traveler")
    public ResponseEntity<APIResponseDTO> registerTraveler(@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(new APIResponseDTO(
                200,
                    "Success",
                authService.registerTraveler(registerDTO)
        ));
    }
    @PostMapping("/register/agency")
    public ResponseEntity<APIResponseDTO> registerAgency(@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(new APIResponseDTO(
                200,
                "Success",
                authService.registerAgency(registerDTO)
        ));
    }
}
