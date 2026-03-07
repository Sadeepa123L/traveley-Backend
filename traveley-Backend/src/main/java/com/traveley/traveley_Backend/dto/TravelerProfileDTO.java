package com.traveley.traveley_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TravelerProfileDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String country;
}
