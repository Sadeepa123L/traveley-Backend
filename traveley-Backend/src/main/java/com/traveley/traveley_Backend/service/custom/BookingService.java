package com.traveley.traveley_Backend.service.custom;

import com.traveley.traveley_Backend.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    void bookTourPackage(BookingDTO bookingDTO, String username, Long id);
    List<BookingResponseDTO> getAllBooking(String username);
    void confirmBooking(String username, Long id);
    List<TopPackageDTO> getTopPackages();
    List<ChartDataDTO> getWeeklyRevenueChart();
}
