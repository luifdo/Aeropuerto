package com.pw.aerropuerto.dominio.repositories;

import com.pw.aerropuerto.dominio.entities.Airport;
import com.pw.aerropuerto.dominio.entities.Booking;
import com.pw.aerropuerto.dominio.entities.Passenger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AirportRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private AirportRepository airportRepository;

    @Test
    @DisplayName("Debe encontrar un aeropuerto por su código")
    void shouldFindAirportByCode() {
        Airport airport = Airport.builder()
                .Name("El Dorado International Airport")
                .Code("BOG")
                .City("Bogotá")
                .build();

        airportRepository.save(airport);

        Optional<Airport> found = airportRepository.findByCode("BOG");

        assertThat(found).isPresent();
        assertThat(found.get().getCode()).isEqualTo("BOG");
        assertThat(found.get().getName()).isEqualTo("El Dorado International Airport");
    }

    @Test
    @DisplayName("No debe encontrar aeropuerto con código inexistente")
    void shouldNotFindAirportByInvalidCode() {

        Optional<Airport> found = airportRepository.findByCode("XXX");

        assertThat(found).isNotPresent();
    }

}