package com.traveley.traveley_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;



@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingDTO {
    private Long id;
    private LocalDate travelDate;
    private String status;
    private Long travelerId;
    private Long agencyId;
    private Long tourPackageId;

    private BookingDetailsDTO bookingDetails;

}
