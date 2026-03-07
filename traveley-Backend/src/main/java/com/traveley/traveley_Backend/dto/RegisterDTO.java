package com.traveley.traveley_Backend.dto;

import com.traveley.traveley_Backend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String status;
    private Role role;
}
