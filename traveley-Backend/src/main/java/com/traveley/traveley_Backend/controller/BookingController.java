package com.traveley.traveley_Backend.controller;

import com.traveley.traveley_Backend.dto.BookingDTO;
import com.traveley.traveley_Backend.service.custom.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/v1/booking")
@RestController
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/bookTourPackage")
    public ResponseEntity<?> createBooking(@RequestBody BookingDTO bookingDTO, Principal principal) {
        try {
            String username = principal.getName();

            Long id = bookingDTO.getTourPackageId();

            bookingService.bookTourPackage(bookingDTO, username, id);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Booking request sent successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred while processing your booking."));
        }
    }
}
