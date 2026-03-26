package com.traveley.traveley_Backend.repository;

import com.traveley.traveley_Backend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Long> {
    List<Booking> findAllByAgencyProfile_Id(Long id);
    List<Booking> findByTravelDateBetween(LocalDate startDate, LocalDate endDate);
}
