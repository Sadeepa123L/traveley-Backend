package com.traveley.traveley_Backend.service.custom;

import com.traveley.traveley_Backend.dto.BookingDTO;
import org.springframework.stereotype.Service;

@Service
public interface BookingService {
    void bookTourPackage(BookingDTO bookingDTO, String username, Long id);
}
