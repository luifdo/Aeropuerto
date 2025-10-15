package com.pw.aerropuerto.dominio.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
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

    @OneToMany(mappedBy = "booking")
    private List<BookItem> items;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    public void addItem(BookItem item) {
        items.add(item);
        item.setBooking(this);
    }

}
