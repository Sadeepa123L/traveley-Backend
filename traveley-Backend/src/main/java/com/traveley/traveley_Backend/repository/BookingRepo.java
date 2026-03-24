package com.traveley.traveley_Backend.repository;

import com.traveley.traveley_Backend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepo extends JpaRepository<Booking, Long> {
}
