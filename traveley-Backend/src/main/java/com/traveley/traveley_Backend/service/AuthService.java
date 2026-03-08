package com.traveley.traveley_Backend.service;

import com.traveley.traveley_Backend.dto.AuthDTO;
import com.traveley.traveley_Backend.dto.AuthResponseDTO;
import com.traveley.traveley_Backend.dto.RegisterDTO;
import com.traveley.traveley_Backend.entity.Role;
import com.traveley.traveley_Backend.entity.User;
import com.traveley.traveley_Backend.repository.UserRepo;
import com.traveley.traveley_Backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponseDTO authenticate(AuthDTO authDTO){
        User user = userRepo.findByUsername(authDTO.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException(authDTO.getUsername())
        );
        if (!passwordEncoder.matches(authDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(authDTO.getUsername());
        }

        String token = jwtUtil.generateToken(authDTO.getUsername());
        return new AuthResponseDTO(token);
    }

    public String registerTraveler(RegisterDTO registerDTO){
        checkUserExists(registerDTO.getUsername());

        User user = User.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .status("ACTIVE")
                .role(Role.TRAVELER)
                .build();
        userRepo.save(user);
        return("Traveler registered successfully");
    }

    public String registerAgency(RegisterDTO registerDTO){
        checkUserExists(registerDTO.getUsername());

        User user = User.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .status("PENDING")
                .role(Role.AGENCY)
                .build();
        userRepo.save(user);
        return("Agency registration submitted. Please wait for admin approval.");
    }
    private void checkUserExists(String username) {
        if (userRepo.findByUsername(username).isPresent()) {
            throw new RuntimeException("Error: Username is already taken!");
        }
    }
}
