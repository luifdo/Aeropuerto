package com.pw.aerropuerto.dominio.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seatInventory")
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "flight_id")
    private Flight flight;

}
