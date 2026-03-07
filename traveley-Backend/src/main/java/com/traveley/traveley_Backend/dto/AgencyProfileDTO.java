package com.traveley.traveley_Backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AgencyProfileDTO {
    private Long id;
    private String agencyName;
    private String registrationNumber;
    private String contactNumber;
    private String address;
    private String photoUrl;
}
