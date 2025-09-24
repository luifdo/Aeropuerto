package com.pw.aerropuerto.dominio.repositories;

import com.pw.aerropuerto.dominio.entities.Booking;
import com.pw.aerropuerto.dominio.entities.Passenger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import static org.assertj.core.api.Assertions.assertThat;
import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;



public class BookingRespositoryTest extends AbstractRepositoryIT {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    @DisplayName("")
    void testGetBookingsByEmail() {

        Passenger passenger = new Passenger();
        passenger.setEmail("test@example.com");
        passenger = entityManager.persistAndFlush(passenger);

        Booking booking = new Booking();
        booking.setPassenger(passenger);

        booking.setCreatedAt(OffsetDateTime.now());
        booking = entityManager.persistAndFlush(booking);


        entityManager.clear();

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Booking> bookings = bookingRepository.getBookingsByEmail("TEST@EXAMPLE.COM", pageable);


        assertThat(bookings).isNotNull();
        assertThat(bookings.getContent()).isNotEmpty();
        assertThat(bookings.getContent().get(0).getPassenger()).isNotNull();
        assertThat(bookings.getContent().get(0).getPassenger().getEmail()).isEqualTo("test@example.com");}
}