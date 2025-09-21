package com.pw.aerropuerto.dominio.repositories;


import com.pw.aerropuerto.dominio.entities.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;



public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "JOIN b.passenger p " +
            "WHERE lower(p.email) = lower(:email) " +
            "ORDER BY b.createdAt DESC")
    Page<Booking> getBookingsByEmail(@Param("email") String email, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "left join fetch b.Items i " +
            "left join fetch i.flight f " +
            "left join fetch b.passenger p " +
            "where b.id = :id ")
        Booking getBookingById(@Param("id") Long id);
}
