package com.traveley.traveley_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TopPackageDTO {
    private Long id;
    private String name;
    private Long totalBookings;
    private String image;
}
