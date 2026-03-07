package com.traveley.traveley_Backend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AgencyProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String agencyName;

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    @Column(nullable = false)
    private String contactNumber;

    private String address;
    private String photoUrl;

    @OneToMany(mappedBy = "agencyProfile", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "agencyProfile", cascade = CascadeType.ALL)
    private List<TourPackage> tourPackages;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
