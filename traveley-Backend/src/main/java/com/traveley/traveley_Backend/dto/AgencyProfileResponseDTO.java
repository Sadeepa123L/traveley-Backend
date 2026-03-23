package com.traveley.traveley_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AgencyProfileResponseDTO {
    private Long id;
    private String name;
    private String contactNumber;
    private String registrationNumber;
    private String status;
}
