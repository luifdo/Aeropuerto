package com.pw.aerropuerto.dominio.repositories;

import com.pw.aerropuerto.dominio.entities.Airport;
import com.pw.aerropuerto.dominio.entities.Cabin;
import com.pw.aerropuerto.dominio.entities.Flight;
import com.pw.aerropuerto.dominio.entities.SeatInventory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SeatInventoryRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private SeatInventoryRepository seatInventoryRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirportRepository airportRepository;

    private Flight createFlight() {
        var origin = airportRepository.save(Airport.builder()
                .Code("BOG")
                .Name("El Dorado")
                .build());

        var destination = airportRepository.save(Airport.builder()
                .Code("MAD")
                .Name("Barajas")
                .build());

        return flightRepository.save(Flight.builder()
                .number("AV345")
                .origin(origin)
                .destination(destination)
                .build());
    }

    @Test
    @DisplayName("encuentra inventario por vuelo y cabina")
    void findByFlightIdAndCabinQuery() {

         var flight = createFlight();
        seatInventoryRepository.save(SeatInventory.builder()
                .flight(flight)
                .Cabin(Cabin.BUSINESS)
                .availableSeats(5)
                .build());

        assertThat(seatInventoryRepository.findByFlightIdAndCabinQuery(flight.getId(), Cabin.BUSINESS))
                .isPresent();
    }

    @Test
    @DisplayName("verifica si hay asientos disponibles")
    void hasAvailableSeatsNative() {
        var flight = createFlight();
        seatInventoryRepository.save(SeatInventory.builder()
                .flight(flight)
                .Cabin(Cabin.BUSINESS)
                .availableSeats(5)
                .build());

        assertThat(seatInventoryRepository.hasAvailableSeatsNative(flight.getId(), Cabin.BUSINESS.name(), 10))
                .isFalse();

        assertThat(seatInventoryRepository.hasAvailableSeatsNative(flight.getId(), Cabin.BUSINESS.name(), 3))
                .isTrue();
    }
}