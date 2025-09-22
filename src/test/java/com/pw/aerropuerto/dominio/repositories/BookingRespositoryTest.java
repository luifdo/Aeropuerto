package com.pw.aerropuerto.dominio.repositories;

import com.pw.aerropuerto.dominio.entities.Booking;
import com.pw.aerropuerto.dominio.entities.Passenger;
import lombok.var;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BookingRespositoryTest extends AbstractRepositoryIT{
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    @DisplayName("")
    void testGetBookingsByEmail() {

        var

        Passenger passenger = new Passenger();
        passenger.setEmail("test@example.com");
        entityManager.persist(passenger);

        // Crear booking asociado
        Booking booking = new Booking();
        booking.setPassenger(passenger);
        booking.setCreatedAt(LocalDateTime.now());
        entityManager.persist(booking);

        entityManager.flush();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> bookings = bookingRepository.getBookingsByEmail("TEST@EXAMPLE.COM", pageable);

        assertThat(bookings).isNotEmpty();
        assertThat(bookings.getContent().get(0).getPassenger().getEmail()).isEqualTo("test@example.com");
}
