package com.traveley.traveley_Backend.service.impl;

import com.traveley.traveley_Backend.dto.*;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final TravelerProfileRepo travelerProfileRepo;
    private final TourPackageRepo tourPackageRepo;
    private final AgencyProfileRepo agencyProfileRepo;
    private final ModelMapper modelMapper;
    private final BookingRepo bookingRepo;
    private final EmailService emailService;

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

    @Override
    public List<BookingResponseDTO> getAllBooking(String username) {
        AgencyProfile agencyProfile = agencyProfileRepo.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Agency not found"));

        List<Booking> bookings = bookingRepo.findAllByAgencyProfile_Id(agencyProfile.getId());

        return bookings.stream().map(booking -> {
            BookingResponseDTO response = new BookingResponseDTO();
            response.setId(booking.getId());

                    response.setTourPackageName(booking.getTourPackage().getTitle());
                    response.setTravelerName(booking.getBookingDetails().getContactName());
                    response.setGuestCount(booking.getBookingDetails().getGuestCount());
                    response.setTravelDate(String.valueOf(booking.getTravelDate()));
                    response.setStatus(booking.getStatus());
                    response.setGuestCount(booking.getBookingDetails().getGuestCount());
                    response.setTotalPrice(booking.getTotalPrice());
                    response.setEmail(booking.getBookingDetails().getEmail());
                    response.setMobileNumber(booking.getBookingDetails().getContactNumber());

                    return response;
                }).collect(Collectors.toList());
    }

    @Override
    public void confirmBooking(String username, Long id) {

        AgencyProfile agencyProfile = agencyProfileRepo.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Agency not found"));

        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if(!booking.getAgencyProfile().getId().equals(agencyProfile.getId())){
            throw new RuntimeException("You are not authorized to confirm this booking");
        }

        booking.setStatus("CONFIRMED");
        bookingRepo.save(booking);

        sendConfirmationEmail(booking);
    }

    @Override
    public List<TopPackageDTO> getTopPackages() {

        List<Booking> allBookings = bookingRepo.findAll();

        Map<TourPackage, Long> packageCounts = allBookings.stream()
                .collect(Collectors.groupingBy(Booking::getTourPackage, Collectors.counting()));

        return packageCounts.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .limit(3)
                .map(entry -> {
                    TourPackage tourPackage = entry.getKey();
                    Long count = entry.getValue();

                    return new TopPackageDTO(
                            tourPackage.getId(),
                            tourPackage.getTitle(),
                            count,
                            tourPackage.getImageUrl()
                    );
                })
                .collect(Collectors.toList());
    }

    private void sendConfirmationEmail(Booking booking) {
        try {
            String travelerEmail = booking.getBookingDetails().getEmail();
            String travelerName = booking.getBookingDetails().getContactName();
            String packageName = booking.getTourPackage().getTitle();
            Long bookingId = booking.getId();

            emailService.sendBookingConfirmation(travelerEmail, travelerName, packageName, bookingId);
            System.out.println("Email sent successfully to: " + travelerEmail);
        } catch (Exception e) {
            System.err.println("Failed to send confirmation email: " + e.getMessage());
        }
    }
}