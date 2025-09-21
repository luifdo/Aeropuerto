package com.pw.aerropuerto.dominio.repositories;

import com.pw.aerropuerto.dominio.entities.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    //Query para encontrar pasajero por su email
    @Query(
            "SELECT p FROM Passenger p WHERE LOWER(p.email) = LOWER(:email)"
    )
    Optional<Passenger> findByEmailIgnoreCaseQuery(@Param("email") String email);

    //Query que busca un pasajero por email, ignorando mayúsculas/minúsculas, pero precarga (fetch) el PassengerProfile para evitar N+1. (JPQL con LEFT JOIN FETCH)
    @Query("SELECT p FROM Passenger p LEFT JOIN FETCH p.profile "
            + "WHERE LOWER(p.email) = LOWER(:email)")
    Optional<Passenger> findByEmailIgnoreCaseWithProfile(@Param("email") String email);
}
