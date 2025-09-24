package com.pw.aerropuerto.dominio.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "passengers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Passenger {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;

    @OneToOne(mappedBy = "passenger")
    private PassengerProfile passengerProfile;

    @OneToMany(mappedBy = "passenger")
    private Set<Booking> bookings;
}
