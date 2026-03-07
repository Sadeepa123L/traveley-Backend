package com.traveley.traveley_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingDTO {
    private Long bookingId;
    private LocalDate travelDate;
    private String status;
    private Long travelerId;
    private Long agencyId;
    private Long tourPackageId;
    private double totalPrice;

    private List<BookingDetailsDTO> bookingDetails;

}
