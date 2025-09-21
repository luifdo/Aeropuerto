package com.pw.aerropuerto.dominio.repositories;

import com.pw.aerropuerto.dominio.entities.Cabin;
import com.pw.aerropuerto.dominio.entities.SeatInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SeatInventoryRepository extends JpaRepository<SeatInventory, Long> {
    @Query("SELECT s FROM SeatInventory s WHERE s.flight.id = :flightId AND s.Cabin = :cabin")
    Optional<SeatInventory> findByFlightIdAndCabinQuery(@Param("flightId") Long flightId,
                                                        @Param("cabin") Cabin cabin);
    @Query(value = "SELECT EXISTS (" +
            "SELECT 1 FROM seat_inventory si " +
            "WHERE si.flight_id = :flightId " +
            "  AND si.cabin = :cabin " +
            "  AND si.available_seats >= :min)",
            nativeQuery = true)
    boolean hasAvailableSeatsNative(@Param("flightId") Long flightId,
                                    @Param("cabin") String cabin,
                                    @Param("min") int min);
}
