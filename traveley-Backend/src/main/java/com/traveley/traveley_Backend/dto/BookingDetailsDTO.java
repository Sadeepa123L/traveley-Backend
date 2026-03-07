package com.traveley.traveley_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingDetailsDTO {
    private String contactName;
    private String contactNumber;
    private LocalDateTime bookingDate;
    private Integer guestCount;
    private String specialRequest;
}
