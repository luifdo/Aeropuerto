package com.pw.aerropuerto.dominio.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seatInventory", uniqueConstraints = @UniqueConstraint(name = "uk_seat_inventory_flight_cabin",
        columnNames = {"flight_id", "cabin"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Cabin Cabin;
    @Column(nullable = false)
    private Integer totalSeats;
    @Column(nullable = false)
    private Integer availableSeats;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id")
    private Flight flight;

}
