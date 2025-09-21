package com.pw.aerropuerto.dominio.repositories;

import com.pw.aerropuerto.dominio.entities.BookItem;
import com.pw.aerropuerto.dominio.entities.Cabin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BookItemRepository extends JpaRepository<BookItem, Long> {

    @Query("SELECT bi " +
            "FROM BookItem bi " +
            "WHERE bi.booking.id = :bookingId " +
            "ORDER BY bi.segmentOrder ASC")
    List<BookItem> findItemsByBookingIdOrdered(@Param("bookingId") Long bookingId);

    @Query("SELECT COALESCE(SUM(bi.price), 0) " +
            "FROM BookItem bi " +
            "WHERE bi.booking.id = :bookingId")
    BigDecimal calculateTotalByBookingId(@Param("bookingId") Long bookingId);

    @Query("SELECT count(bi) from BookItem bi " +
            "join bi.flight f " +
            "where f.id = :flight and bi.cabin = :cabin ")
    long countReservedSeatsByFlightAndCabin(@Param("flightId") Long flightId,@Param("cabin") Cabin cabin);


}
