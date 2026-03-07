package com.traveley.traveley_Backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TravelerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String country;

    @OneToMany(mappedBy = "travelerProfile", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
