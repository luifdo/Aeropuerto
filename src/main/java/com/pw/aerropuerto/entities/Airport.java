package com.pw.aerropuerto.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@Table(name = "airports")
public class Airport {

    @Id @GeneratedValue (strategy = GenerationType.AUTO)

    @Column(nullable = false)
    private Long id;


    @Column(nullable = false)
    String Code;
    @Column(nullable = false)
    String Name;
    @Column(nullable = false)
    String City;

    @ManyToMany
    @JoinTable(
            name = "AIRPORT_ID",
            joinColumns = @JoinColumn(name = "AIRPORT_ID", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "FLIGHT_ID",referencedColumnName = "id")
    )
    private List<Flight> flights = new ArrayList<>();

    public void addFlight(Flight flight) {
        this.flights.add(flight);
    }



}
