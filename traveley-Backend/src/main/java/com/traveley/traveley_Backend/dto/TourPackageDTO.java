package com.traveley.traveley_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TourPackageDTO {
    private Long id;
    private String title;
    private String description;
    private String destination;
    private Double price;
    private Integer duration;
    private String imageUrl;
}
