package com.traveley.traveley_Backend.controller;

import com.traveley.traveley_Backend.dto.BookingDTO;
import com.traveley.traveley_Backend.dto.BookingResponseDTO;
import com.traveley.traveley_Backend.dto.ChartDataDTO;
import com.traveley.traveley_Backend.dto.TopPackageDTO;
import com.traveley.traveley_Backend.service.custom.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
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

    @GetMapping("/getAllBookings")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings(Principal principal) {
        try {
            List<BookingResponseDTO> bookings = bookingService.getAllBooking(principal.getName());
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PutMapping("/updateStatus/{id}")
    public  ResponseEntity<?> updateStatus(@PathVariable Long id, Principal principal) {
        try{
            bookingService.confirmBooking(principal.getName(), id);
            return ResponseEntity.ok("Booking confirmed successfully!");
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong: " + e.getMessage());
        }
    }
    @GetMapping("/getTopPackages")
    public ResponseEntity<List<TopPackageDTO>> getTopPackages(){
        List<TopPackageDTO> topPackages = bookingService.getTopPackages();
        return ResponseEntity.ok(topPackages);
    }
    @GetMapping("/getWeeklyRevenue")
    public ResponseEntity<List<ChartDataDTO>> getWeeklyRevenue(){
        List<ChartDataDTO> weeklyRevenue = bookingService.getWeeklyRevenueChart();
        return ResponseEntity.ok(weeklyRevenue);
    }
}
