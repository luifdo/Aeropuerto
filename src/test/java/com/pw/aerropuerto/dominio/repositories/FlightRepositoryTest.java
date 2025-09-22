package com.pw.aerropuerto.dominio.repositories;

import com.pw.aerropuerto.dominio.entities.Airline;
import com.pw.aerropuerto.dominio.entities.Airport;
import com.pw.aerropuerto.dominio.entities.Flight;
import com.pw.aerropuerto.dominio.entities.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class FlightRepositoryTest extends AbstractRepositoryIT{
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("Encuentra vuelos por nombre de aerolínea")
    void findByAirlineName_returnsFlights() {
        var airline = airlineRepository.save(Airline.builder()
                .name("Avianca")
                .build());

        var flight1 = flightRepository.save(Flight.builder()
                .id(2L)
                .airline(airline)
                .build());

        var flight2 = flightRepository.save(Flight.builder()
                .id(4L)
                .airline(airline)
                .build());


        Page<Flight> result = flightRepository.findByAirlineName(
                "avianca", PageRequest.of(0, 10));


        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent())
                .extracting(Flight::getId)
                .containsExactlyInAnyOrder(2L, 4L);
    }

    @Test
    @DisplayName("No devuelve vuelos si el nombre de aerolínea no coincide")
    void findByAirlineName_returnsEmptyWhenNotMatch() {
        var airline = airlineRepository.save(Airline.builder()
                .name("LATAM")
                .build());

        flightRepository.save(Flight.builder()
                .id(5L)
                .airline(airline)
                .build());

        Page<Flight> result = flightRepository.findByAirlineName(
                "avianca", PageRequest.of(0, 10));

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }
    @Test
    @DisplayName("findFlightsByRouteAndWindow - devuelve vuelos cuya departureTime está dentro de la ventana (incluye límites)")
    void findFlightsByRouteAndWindow_returnsFlightsInsideWindow() {

        var origin = airportRepository.save(Airport.builder()
                .Name("Origin Airport")
                .Code("ORY")
                .build());

        var destination = airportRepository.save(Airport.builder()
                .Name("Destination Airport")
                .Code("DST")
                .build());


        LocalDateTime from = LocalDateTime.of(2025, 9, 20, 8, 0);
        LocalDateTime to   = LocalDateTime.of(2025, 9, 20, 12, 0);


        flightRepository.save(Flight.builder()
                .id(0L)
                .origin(origin)
                .destination(destination)
                .departureTime(OffsetDateTime.from(from.minusMinutes(1))) // fuera (antes)
                .build());

        var flightInside = flightRepository.save(Flight.builder()
                .id(1L)
                .origin(origin)
                .destination(destination)
                .departureTime(OffsetDateTime.from(from.plusHours(2))) // dentro
                .build());

        var flightAtFrom = flightRepository.save(Flight.builder()
                .id(2L)
                .origin(origin)
                .destination(destination)
                .departureTime(OffsetDateTime.from(from))
                .build());

        var flightAtTo = flightRepository.save(Flight.builder()
                .id(3L)
                .origin(origin)
                .destination(destination)
                .departureTime(OffsetDateTime.from(to)) // límite superior
                .build());

        flightRepository.save(Flight.builder()
                .id(4L)
                .origin(origin)
                .destination(destination)
                .departureTime(OffsetDateTime.from(to.plusMinutes(1))) // fuera (después)
                .build());

        // ejecutar query
        Page<Flight> result = flightRepository.findFlightsByRouteAndWindow(
                "ORY", "DST", from, to, PageRequest.of(0, 10));

        // verificaciones
        assertThat(result).isNotNull();
        // esperamos 3: F-INSIDE, F-AT-FROM, F-AT-TO (BETWEEN es inclusivo)
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent())
                .extracting(Flight::getId)
                .containsExactlyInAnyOrder(0L, 1L, 2L);
    }

    @Test
    @DisplayName("findFlightsByRouteAndWindow - no devuelve vuelos si ninguno cae en la ventana")
    void findFlightsByRouteAndWindow_returnsEmptyWhenNoneInWindow() {
        var origin = airportRepository.save(Airport.builder()
                .Name("Origin Airport 2")
                .Code("AAA")
                .build());

        var destination = airportRepository.save(Airport.builder()
                .Name("Destination Airport 2")
                .Code("BBB")
                .build());

        // ventana de búsqueda
        LocalDateTime from = LocalDateTime.of(2025, 10, 1, 6, 0);
        LocalDateTime to   = LocalDateTime.of(2025, 10, 1, 10, 0);


        flightRepository.save(Flight.builder()
                .id(5L)
                .origin(origin)
                .destination(destination)
                .departureTime(OffsetDateTime.from(from.minusDays(1)))
                .build());

        flightRepository.save(Flight.builder()
                .id(6L)
                .origin(origin)
                .destination(destination)
                .departureTime(OffsetDateTime.from(to.plusDays(1)))
                .build());

        Page<Flight> result = flightRepository.findFlightsByRouteAndWindow(
                "AAA", "BBB", from, to, PageRequest.of(0, 10));

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }
    @Test
    @DisplayName("findByOptionalOriginDestinationAndDepartureWindowWithFetch - filtra por origen, destino y ventana, trae asociaciones y no duplica por tags")
    void findByOptionalOriginDestinationAndDepartureWindowWithFetch_returnsDistinctFlightsWithFetchedAssociations() {
        // preparar datos
        var origin = airportRepository.save(Airport.builder()
                .Name("Origin A")
                .Code("OAA")
                .build());

        var destination = airportRepository.save(Airport.builder()
                .Name("Destination B")
                .Code("DBB")
                .build());

        var otherDestination = airportRepository.save(Airport.builder()
                .Name("Destination C")
                .Code("DCC")
                .build());

        var airline = airlineRepository.save(Airline.builder()
                .name("MyAir")
                .build());

        // tags
        var tag1 = tagRepository.save(Tag.builder().name("RED-EYE").build());
        var tag2 = tagRepository.save(Tag.builder().name("DOMESTIC").build());

        // ventana de búsqueda
        LocalDateTime from = LocalDateTime.of(2025, 9, 20, 0, 0);
        LocalDateTime to   = LocalDateTime.of(2025, 9, 21, 23, 59);

        // flight con dos tags (puede provocar duplicado si no hubiera DISTINCT)
        var flightWithTwoTags = Flight.builder()
                .id(0L)
                .origin(origin)
                .destination(destination)
                .airline(airline)
                .departureTime(OffsetDateTime.from(LocalDateTime.of(2025, 9, 20, 10, 0)))
                .build();
        // asignar tags (si el builder tiene .tags(...) úsalo; si no, usa addTag o setTags)
        flightWithTwoTags.setTags(Set.of(tag1, tag2));
        flightRepository.save(flightWithTwoTags);

        // otro vuelo distinto que también cumple ruta/ventana
        var flightOther = Flight.builder()
                .id(1L)
                .origin(origin)
                .destination(destination)
                .airline(airline)
                .departureTime(OffsetDateTime.from(LocalDateTime.of(2025, 9, 20, 15, 0)))
                .build();
        flightOther.setTags(Set.of(tag1));
        flightRepository.save(flightOther);

        // vuelo con mismo origen pero distinto destino -> no debe incluirse
        flightRepository.save(Flight.builder()
                .id(2L)
                .origin(origin)
                .destination(otherDestination)
                .airline(airline)
                .departureTime(OffsetDateTime.from(LocalDateTime.of(2025, 9, 20, 11, 0)))
                .build());

        // ejecución: filtrar por origen OAA y destino DBB
        List<Flight> results = flightRepository.findByOptionalOriginDestinationAndDepartureWindowWithFetch(
                "OAA", "DBB", from, to);

        // verificaciones básicas
        assertThat(results).isNotNull();
        // esperamos 2 vuelos: F-01 y F-02
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Flight::getId)
                .containsExactlyInAnyOrder(0L, 1L);

        // comprobar que las asociaciones fueron cargadas (no-null)
        for (Flight f : results) {
            assertThat(f.getAirline()).isNotNull();
            assertThat(f.getOrigin()).isNotNull();
            assertThat(f.getDestination()).isNotNull();
            // tags pueden variar por vuelo; el flightWithTwoTags debe tener 2 tags
            if (f.getId().equals(0L)) {
                assertThat(f.getTags()).hasSize(2);
                assertThat(f.getTags()).extracting(Tag::getName).containsExactlyInAnyOrder("RED-EYE", "DOMESTIC");
            } else if (f.getId().equals(1L)) {
                assertThat(f.getTags()).hasSize(1);
            }
        }
    }

    @Test
    @DisplayName("findByOptionalOriginDestinationAndDepartureWindowWithFetch - parámetros opcionales null permiten 'ignorar' ese filtro")
    void findByOptionalOriginDestinationAndDepartureWindowWithFetch_allowsNullOriginAndDestination() {
        var origin = airportRepository.save(Airport.builder().Name("X").Code("XXX").build());
        var destination = airportRepository.save(Airport.builder().Name("Y").Code("YYY").build());
        var airline = airlineRepository.save(Airline.builder().name("AirX").build());

        LocalDateTime from = LocalDateTime.of(2025, 11, 1, 0, 0);
        LocalDateTime to   = LocalDateTime.of(2025, 11, 2, 23, 59);

        flightRepository.save(Flight.builder()
                .id(0L)
                .origin(origin)
                .destination(destination)
                .airline(airline)
                .departureTime(OffsetDateTime.from(LocalDateTime.of(2025, 11, 1, 12, 0)))
                .build());

        // ejecutar con o = null y d = null -> solo filtra por departureTime BETWEEN
        List<Flight> results = flightRepository.findByOptionalOriginDestinationAndDepartureWindowWithFetch(
                null, null, from, to);

        assertThat(results).isNotNull();
        assertThat(results).isNotEmpty();
        assertThat(results).extracting(Flight::getId).contains(1L);
    }
    @Test
    @DisplayName("findFlightsHavingAllTagNamesNative - devuelve vuelos que contienen todos los nombres de tag pedidos")
    void findFlightsHavingAllTagNamesNative_returnsFlightsWithAllTags() {
        // preparar datos mínimos necesarios para crear Flight
        var origin = airportRepository.save(Airport.builder().Name("Orig").Code("OGG").build());
        var destination = airportRepository.save(Airport.builder().Name("Dest").Code("DST").build());
        var airline = airlineRepository.save(Airline.builder().name("AirTest").build());

        // crear tags
        var tagA = tagRepository.save(Tag.builder().name("A").build());
        var tagB = tagRepository.save(Tag.builder().name("B").build());
        var tagC = tagRepository.save(Tag.builder().name("C").build()); // tag extra

        // Flight que tiene A y B (y además C) -> cumple (tiene al menos A y B)
        var flightWithAll = Flight.builder()
                .id(1L)
                .origin(origin)
                .destination(destination)
                .airline(airline)
                .departureTime(OffsetDateTime.from(LocalDateTime.now().plusDays(1)))
                .build();
        flightWithAll.setTags(Set.of(tagA, tagB, tagC)); // tiene A,B,C
        flightRepository.save(flightWithAll);

        // Flight que tiene sólo A -> no cumple
        var flightMissingB = Flight.builder()
                .id(2L)
                .origin(origin)
                .destination(destination)
                .airline(airline)
                .departureTime(OffsetDateTime.from(LocalDateTime.now().plusDays(1)))
                .build();
        flightMissingB.setTags(Set.of(tagA));
        flightRepository.save(flightMissingB);

        // parámetros de búsqueda: queremos vuelos que tengan BOTH "A" y "B"
        Collection<String> names = List.of("A", "B");
        long required = 2L; // debe coincidir con distinct names size

        List<Flight> results = flightRepository.findFlightsHavingAllTagNamesNative(names, required);

        // verificaciones
        assertThat(results).isNotNull();
        assertThat(results).hasSize(1);
        assertThat(results).extracting(Flight::getId).containsExactly(2L);
    }

    @Test
    @DisplayName("findFlightsHavingAllTagNamesNative - no devuelve vuelos que falten al menos un tag requerido")
    void findFlightsHavingAllTagNamesNative_doesNotReturnWhenMissingTag() {
        // preparar datos mínimos
        var origin = airportRepository.save(Airport.builder().Name("Orig2").Code("O2").build());
        var destination = airportRepository.save(Airport.builder().Name("Dest2").Code("D2").build());
        var airline = airlineRepository.save(Airline.builder().name("AirTwo").build());

        // tags
        var tagX = tagRepository.save(Tag.builder().name("X").build());
        var tagY = tagRepository.save(Tag.builder().name("Y").build());

        // Flight que tiene solo X (falta Y)
        var flight = Flight.builder()
                .id(3L)
                .origin(origin)
                .destination(destination)
                .airline(airline)
                .departureTime(OffsetDateTime.from(LocalDateTime.now().plusDays(2)))
                .build();
        flight.setTags(Set.of(tagX));
        flightRepository.save(flight);

        Collection<String> names = List.of("X", "Y");
        long required = 2L;

        List<Flight> results = flightRepository.findFlightsHavingAllTagNamesNative(names, required);

        assertThat(results).isNotNull();
        assertThat(results).isEmpty();
    }
}
