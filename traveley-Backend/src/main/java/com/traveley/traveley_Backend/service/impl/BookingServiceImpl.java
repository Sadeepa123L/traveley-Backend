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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
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
     Optional<AgencyProfile> agencyProfile = agencyProfileRepo.findByUser_Username(username);

     if(agencyProfile.isEmpty()){
         return new ArrayList<>();
     }

        List<Booking> bookings = bookingRepo.findAllByAgencyProfile_Id(agencyProfile.get().getId());

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
    public List<TopPackageDTO> getTopPackages(String username) {

        Optional<AgencyProfile> agencyProfile = agencyProfileRepo.findByUser_Username(username);

        if (agencyProfile.isEmpty()) {
            return new ArrayList<>();
        }

        Long agencyId = agencyProfile.get().getId();

        List<Booking> allBookings = bookingRepo.findAllByAgencyProfile_Id(agencyId);

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

    @Override
    public List<ChartDataDTO> getWeeklyRevenueChart(String username) {

        Optional<AgencyProfile> agencyProfile = agencyProfileRepo.findByUser_Username(username);

        if (agencyProfile.isEmpty()) {
            List<ChartDataDTO> emptyChart = new ArrayList<>();
            for(DayOfWeek day : DayOfWeek.values()){
                String dateName = day.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
                emptyChart.add(new ChartDataDTO(dateName, 0, 0.0));
            }
            return emptyChart;
        }

        Long agencyId = agencyProfile.get().getId();

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<Booking> allBookings = bookingRepo.findAllByAgencyProfile_Id(agencyId);

        Map<DayOfWeek, ChartDataDTO> dailyStats = new LinkedHashMap<>();
        for(DayOfWeek day : DayOfWeek.values()){
            String dateName = day.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            dailyStats.put(day, new ChartDataDTO(dateName, 0, 0.0));
        }
        for(Booking booking : allBookings){
            LocalDate bookingDate = booking.getBookingDetails().getBookingDate().toLocalDate();

            if (!bookingDate.isBefore(startOfWeek) && !bookingDate.isAfter(endOfWeek)) {
                DayOfWeek dayOfWeek = bookingDate.getDayOfWeek();
                ChartDataDTO chartDataDTO = dailyStats.get(dayOfWeek);

                chartDataDTO.setTotalBookings(chartDataDTO.getTotalBookings() + 1);
                chartDataDTO.setRevenue(chartDataDTO.getRevenue() + booking.getTotalPrice());
            }
        }

        return new ArrayList<>(dailyStats.values());
    }

    @Override
    public List<BookingResponseDTO> getLatestBooking(String username) {

        Optional<AgencyProfile> agencyProfiles = agencyProfileRepo.findByUser_Username(username);

        if(agencyProfiles.isEmpty()){
            return new ArrayList<>();
        }

        Long agencyId = agencyProfiles.get().getId();

        List<Booking> allBookings = bookingRepo.findAllByAgencyProfile_Id(agencyId);

        return allBookings.stream()
                .sorted(Comparator.comparing((Booking b) -> b.getBookingDetails().getBookingDate()).reversed())
                .limit(5)
                .map(booking -> {
                    BookingResponseDTO dto = new BookingResponseDTO();

                    dto.setId(booking.getId());
                    dto.setTourPackageName(booking.getTourPackage().getTitle());
                    String fullName = booking.getTravelerProfile().getFirstName() + " " + booking.getTravelerProfile().getLastName();

                    dto.setTravelerName(fullName);
                    dto.setTravelDate(booking.getBookingDetails().getBookingDate().toString());
                    dto.setGuestCount(booking.getBookingDetails().getGuestCount());
                    dto.setTotalPrice(booking.getTotalPrice());
                    dto.setStatus(booking.getStatus());
                    dto.setEmail(booking.getBookingDetails().getEmail());
                    dto.setMobileNumber(booking.getTravelerProfile().getMobileNumber());

                    return dto;
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