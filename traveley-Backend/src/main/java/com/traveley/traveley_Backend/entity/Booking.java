package com.traveley.traveley_Backend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate travelDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "traveler_id", nullable = false)
    private TravelerProfile travelerProfile;

    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false)
    private AgencyProfile agencyProfile;

    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    private TourPackage tourPackage;

    private double totalPrice;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private BookingDetails bookingDetails;

    

}
