package com.pw.aerropuerto.dominio.repositories;

import com.pw.aerropuerto.dominio.entities.Airport;
import com.pw.aerropuerto.dominio.entities.Passenger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AirportRepositoryTest extends  AbstractRepositoryIT {

    @Autowired
    private AirportRepository airportRepository;
    @Autowired
    private PassengerRepository passengerRepository;

    @Test
    @DisplayName("Recupere un aeropuerto por su codigo")
    void findByCode()
    {
        void shouldFindBookingsByPassengerEmailIgnoreCase() {
        var passenger = passengerRepository.save(Passenger.builder
                .Email("test@example.com")
                .build());

        var booking = BookingRepository.save(Booking.builder()
                .passenger(passenger)
                .createdAt(LocalDateTime.now())
                .build());

        var pageable = PageRequest.of(0, 10);
        var result = BookingRepository.getBookingsByEmail("TEST@example.com", pageable);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent().get(0).getId()).isEqualTo(booking.getID);
        assertThat(result.getContent().get(0).getPassenger().getEmail()).isEqualTo("test@example.com");

    }

}