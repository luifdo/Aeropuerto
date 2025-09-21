package com.pw.aerropuerto.dominio.repositories;

import com.pw.aerropuerto.dominio.entities.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AirlineRepository extends JpaRepository<Airline, Long> {
@Query("SELECT a From Airline a WHERE a.code = :code")
    Optional<Airline> findByCode(@Param("code") String code);
}
