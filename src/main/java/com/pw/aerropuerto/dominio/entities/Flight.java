package com.pw.aerropuerto.dominio.entities;

import jakarta.persistence.*;
import lombok.*;

import javax.print.attribute.standard.Destination;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "flights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String number;
    @Column(nullable = false)
    private OffsetDateTime departureTime;
    @Column(nullable = false)
    private OffsetDateTime arrivalTime;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id")
    private Airline airline;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_airport_id")
    private Airport origin;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_airport_id")
    private Airport destination;
    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(
            name = "flight_tags",
            joinColumns = @JoinColumn(name = "flight_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    Set<Tag> tags = new HashSet<>();

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getFlights().add(this);
    }

}
