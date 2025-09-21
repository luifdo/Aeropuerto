package com.pw.aerropuerto.dominio.repositories;

import com.pw.aerropuerto.dominio.entities.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AirportRepository extends JpaRepository<Airport, Long> {
@Query("SELECT a From Airport a WHERE a.Code = :code")
Optional<Airport> findByCode(@Param("code") String code);
}
