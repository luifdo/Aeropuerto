package com.pw.aerropuerto.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "booking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private OffsetDateTime createdAt;
    @Column (nullable = false)
    private List<BookItem> Items;
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;
}
