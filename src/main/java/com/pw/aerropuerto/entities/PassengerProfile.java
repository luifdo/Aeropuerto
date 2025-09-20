package com.pw.aerropuerto.entities;

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
    private Long Id;
    private String phone;
    private String countryCode;
    @OneToOne(optional = false)
    @JoinColumn(name ="passenger_id", referencedColumnName = "id")
    private Passenger passenger;
}
