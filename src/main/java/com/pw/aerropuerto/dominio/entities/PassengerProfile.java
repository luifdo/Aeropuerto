package com.pw.aerropuerto.dominio.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "passenger_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerProfile {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long Id;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String countryCode;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name ="passenger_id", referencedColumnName = "id")
    private Passenger passenger;
}
