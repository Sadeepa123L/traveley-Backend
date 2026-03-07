package com.traveley.traveley_Backend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "tour_packages")
public class TourPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String destination;
    private Double price;
    private Integer duration;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false)
    private AgencyProfile agencyProfile;

    @OneToMany(mappedBy = "tourPackage", cascade = CascadeType.ALL)
    private List<Booking> bookings;

}
