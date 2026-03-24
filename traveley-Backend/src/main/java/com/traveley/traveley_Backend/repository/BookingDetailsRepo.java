package com.traveley.traveley_Backend.repository;

import com.traveley.traveley_Backend.entity.BookingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDetailsRepo extends JpaRepository<BookingDetails, Long> {
}
