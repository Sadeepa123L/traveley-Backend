package com.traveley.traveley_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChartDataDTO {
    private String dateName;
    private Integer totalBookings;
    private Double revenue;
}
