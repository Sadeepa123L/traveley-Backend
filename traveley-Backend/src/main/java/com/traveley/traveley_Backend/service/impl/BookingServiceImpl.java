package com.traveley.traveley_Backend.service.impl;

import com.traveley.traveley_Backend.dto.BookingDTO;
import com.traveley.traveley_Backend.dto.BookingDetailsDTO;
import com.traveley.traveley_Backend.entity.*;
import com.traveley.traveley_Backend.repository.AgencyProfileRepo;
import com.traveley.traveley_Backend.repository.BookingRepo;
import com.traveley.traveley_Backend.repository.TourPackageRepo;
import com.traveley.traveley_Backend.repository.TravelerProfileRepo;
import com.traveley.traveley_Backend.service.custom.BookingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final TravelerProfileRepo travelerProfileRepo;
    private final TourPackageRepo tourPackageRepo;
    private final AgencyProfileRepo agencyProfileRepo;
    private final ModelMapper modelMapper;
    private final BookingRepo bookingRepo;

    @Override
    public void bookTourPackage(BookingDTO bookingDTO, String username, Long id) {

        TravelerProfile travelerProfile = travelerProfileRepo.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Traveler not found"));

        TourPackage tourPackage = tourPackageRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("TourPackage not found"));

        AgencyProfile agencyProfile = tourPackage.getAgencyProfile();

        if(agencyProfile == null){
            throw new RuntimeException("Agency not found for this package");
        }
        BookingDetailsDTO bookingDetailsDTO = bookingDTO.getBookingDetails();

        BookingDetails bookingDetails = BookingDetails.builder()
                .contactName(bookingDetailsDTO.getContactName())
                .contactNumber(bookingDetailsDTO.getContactNumber())
                .email(bookingDetailsDTO.getEmail())
                .bookingDate(LocalDateTime.now())
                .guestCount(bookingDetailsDTO.getGuestCount())
                .specialRequest(bookingDetailsDTO.getSpecialRequest())
                .build();

        double totalPrice = tourPackage.getPrice() * bookingDetailsDTO.getGuestCount();

        Booking booking = Booking.builder()
                .travelDate(bookingDTO.getTravelDate())
                .status("PENDING")
                .travelerProfile(travelerProfile)
                .agencyProfile(agencyProfile)
                .tourPackage(tourPackage)
                .totalPrice(totalPrice)
                .bookingDetails(bookingDetails)
                .build();

        bookingDetails.setBooking(booking);
        bookingRepo.save(booking);
    }
}