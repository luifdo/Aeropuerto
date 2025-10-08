package com.pw.aerropuerto.dominio.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name = "airports")
@NoArgsConstructor
public class Airport {

    @Id @GeneratedValue (strategy = GenerationType.AUTO)


    private Long id;
    @Column(name = "iata_code", length = 3, nullable = false, unique = true)
    private String Code;
    @Column(nullable = false)
    private String Name;
    @Column(nullable = false)
    private String City;

    @OneToMany (mappedBy = "Origin")
    @Builder.Default
    private Set<Flight> flights = new HashSet<>();

    @OneToMany (mappedBy = "destination")
    @Builder.Default
    private Set<Flight> destinations = new HashSet<>();

    public void addFlight(Flight flight) {
        flights.add(flight);
        flight.setOrigin(this);
    }
    public void addDestination(Flight flight) {
        destinations.add(flight);
        flight.setDestination(this);
    }



}
