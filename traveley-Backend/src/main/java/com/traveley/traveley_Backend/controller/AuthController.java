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
                "Agency registration submitted. Please wait for admin approval.",
                authService.registerAgency(registerDTO)
        ));
    }
    @PostMapping("/login/traveler")
    public ResponseEntity<APIResponseDTO> authenticateTraveler(@RequestBody AuthDTO authDTO) {
        return ResponseEntity.ok(new APIResponseDTO(
                    200,
                "success",
                authService.authenticateTraveler(authDTO)
        ));
    }
    @PostMapping("/login/agency")
    public ResponseEntity<APIResponseDTO> authenticateAgency(@RequestBody AuthDTO authDTO) {
        try{
            String token = authService.authenticateAgency(authDTO);
            APIResponseDTO response = new APIResponseDTO(
                    200,
                    "Agency login successful!",
                     token
            );
            return ResponseEntity.ok(response);
        }catch (RuntimeException e){
            APIResponseDTO errResponse = new APIResponseDTO(403,e.getMessage(),null);
            return ResponseEntity.status(403).body(errResponse);
        }
    }
}
