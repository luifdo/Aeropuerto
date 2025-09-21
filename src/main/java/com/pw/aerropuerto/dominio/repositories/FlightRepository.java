package com.pw.aerropuerto.dominio.repositories;

import com.pw.aerropuerto.dominio.entities.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    @Query("SELECT f FROM Flight f WHERE LOWER(f.airline.name) = LOWER(:AirLineName)")
    Page<Flight> findByAirlineName(String airlineName, Pageable pageable);
    @Query("SELECT f FROM Flight f " +
            "WHERE f.origin.Code = :originCode " +
            "  AND f.destination.Code= :destinationCode " +
            "  AND f.departureTime BETWEEN :from AND :to")
    Page<Flight> findFlightsByRouteAndWindow(
            @Param("originCode") String originCode,
            @Param("destinationCode") String destinationCode,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable);

    @Query("SELECT DISTINCT f FROM Flight f " +
            "LEFT JOIN FETCH f.airline " +
            "LEFT JOIN FETCH f.origin " +
            "LEFT JOIN FETCH f.destination " +
            "LEFT JOIN FETCH f.tags t " +
            "WHERE (:o IS NULL OR f.origin.Code = :o) " +
            "  AND (:d IS NULL OR f.destination.Code = :d) " +
            "  AND f.departureTime BETWEEN :from AND :to")
    List<Flight> findByOptionalOriginDestinationAndDepartureWindowWithFetch(
            @Param("o") String originCode,
            @Param("d") String destinationCode,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query(
            value = "SELECT f.* FROM flight f " +
                    "JOIN flight_tag ft ON f.id = ft.flight_id " +
                    "JOIN tag t ON t.id = ft.tag_id " +
                    "WHERE t.name IN (:names) " +
                    "GROUP BY f.id " +
                    "HAVING COUNT(DISTINCT t.name) = :required",
            nativeQuery = true)
    List<Flight> findFlightsHavingAllTagNamesNative(
            @Param("names") Collection<String> names,
            @Param("required") long required);
}
