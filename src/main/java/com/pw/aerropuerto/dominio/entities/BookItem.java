package com.pw.aerropuerto.dominio.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bookitems")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookItem {
@Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Cabin cabin;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Integer segmentOrder;
    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;


}
