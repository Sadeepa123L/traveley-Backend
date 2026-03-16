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

    public AuthResponseDTO authenticateTraveler(AuthDTO authDTO){
        User user = userRepo.findByUsername(authDTO.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("Username not found" + authDTO.getUsername())
        );
        if(user.getPassword() == null){
            throw new BadCredentialsException("This account is linked to Google. Please log in using Google.");
        }

        if (!passwordEncoder.matches(authDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(authDTO.getUsername(), user.getRole().name());
        return new AuthResponseDTO(token);
    }

    public String authenticateAgency(AuthDTO authDTO){
        User user = userRepo.findByUsername(authDTO.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("Username not found: " + authDTO.getUsername())
        );
        if(user.getPassword() == null || "OAUTH2_USER".equals(user.getPassword())){
            throw new BadCredentialsException("This account is linked to Google. Please log in using Google.");
        }
        if (!passwordEncoder.matches(authDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if ("AGENCY".equals(user.getRole().name()) && "PENDING".equalsIgnoreCase(user.getStatus())){
            throw new RuntimeException("Your agency account is still pending admin approval.");
        }
        return jwtUtil.generateToken(user.getUsername(), user.getRole().name());
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
    public String handleGoogleTraveler(String username, String password){
        User user = userRepo.findByUsername(username).orElseGet(() ->{
            User newUser = User.builder()
                    .username(username)
                    .password("OAUTH2_USER")
                    .role(Role.TRAVELER)
                    .status("ACTIVE")
                    .build();
            return userRepo.save(newUser);
        });
        return jwtUtil.generateToken(user.getUsername(), user.getRole().name());
    }
    public String handleGoogleAgency(String username, String password){
        User user = userRepo.findByUsername(username).orElseGet(() -> {
            User newUser = User.builder()
                    .username(username)
                    .password("OAUTH2_USER")
                    .role(Role.AGENCY)
                    .status("PENDING")
                    .build();
            return userRepo.save(newUser);
        });
        return jwtUtil.generateToken(user.getUsername(), user.getRole().name());
    }
}
