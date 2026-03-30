package com.traveley.traveley_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingResponseDTO {
    private Long id;
    private String tourPackageName;
    private String travelerName;
    private String travelDate;
    private Integer guestCount;
    private Double totalPrice;
    private String status;
    private String email;
    private String mobileNumber;
}
