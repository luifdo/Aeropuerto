package com.pw.aerropuerto.dominio.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table (name = "airlines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Airline {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)

    @Column(nullable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, length = 2, unique = true)
    private String code;

    @OneToMany (mappedBy = "airline")
    private Set<Flight> flights;

    public void addFlight(Flight flight) {
        flights.add(flight);
        flight.setAirline(this);
    }




}
