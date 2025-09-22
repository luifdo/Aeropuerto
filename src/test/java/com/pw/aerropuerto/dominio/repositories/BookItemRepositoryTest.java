package com.pw.aerropuerto.dominio.repositories;

import com.pw.aerropuerto.dominio.entities.*;
import jakarta.persistence.EntityManager;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;


public class BookItemRepositoryTest extends AbstractRepositoryIT {
    @Autowired
    private BookItemRepository bookItemRepository;

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private EntityManager em;
    @Test
    @DisplayName("Recupera BookItems por bookingId ordenados por segmentOrder asc")
    public void findItemsByBookingIdOrdered_returnsOrderedList() {
        var booking = bookingRepository.save(Booking.builder()
                .id(1L)
                .build());

        var itemA = bookItemRepository.save(BookItem.builder()
                .segmentOrder(2)
                .booking(booking)
                .price(new BigDecimal("50"))
                .build());

        var itemB = bookItemRepository.save(BookItem.builder()
                .segmentOrder(1)
                .booking(booking)
                .price(new BigDecimal("100"))
                .build());

        var itemC = bookItemRepository.save(BookItem.builder()
                .segmentOrder(3)
                .booking(booking)
                .price(new BigDecimal("20"))
                .build());

        List<BookItem> result = bookItemRepository.findItemsByBookingIdOrdered(booking.getId());


        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);


        assertThat(result.get(0).getSegmentOrder()).isEqualTo(1);
        assertThat(result.get(1).getSegmentOrder()).isEqualTo(2);
        assertThat(result.get(2).getSegmentOrder()).isEqualTo(3);

        assertThat(result).extracting(BookItem::getId)
                .containsExactly(itemB.getId(), itemA.getId(), itemC.getId());
    }

    @Test
    @DisplayName("Devuelve lista vacía cuando el booking no tiene BookItems")
    public void findItemsByBookingIdOrdered_returnsEmptyListWhenNoItems() {
        var booking = bookingRepository.save(Booking.builder()
                .id(1L)
                .build());

        List<BookItem> result = bookItemRepository.findItemsByBookingIdOrdered(booking.getId());

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }
    @Test
    @DisplayName("Calcula el total de las reservas ")
    @Rollback
    public void totalReservas() {

        Booking booking = new Booking();
        em.persist(booking);

        BookItem item1 = new BookItem();
        item1.setBooking(booking);
        item1.setPrice(new BigDecimal("10.50"));
        em.persist(item1);

        BookItem item2 = new BookItem();
        item2.setBooking(booking);
        item2.setPrice(new BigDecimal("5.25"));
        em.persist(item2);

        em.flush();

        BigDecimal total = bookItemRepository.calculateTotalByBookingId(booking.getId());
        BigDecimal expected = new BigDecimal("15.75");


        assertEquals(0, expected.compareTo(total), "La suma debe ser 15.75");
    }

    @Test
    @DisplayName("calculateTotalByBookingId returns 0 when no items exist for booking")
    @Rollback
    public void returnsZeroWhenNoItems() {
        // booking sin items
        Booking booking = new Booking();
        em.persist(booking);
        em.flush();

        BigDecimal total = bookItemRepository.calculateTotalByBookingId(booking.getId());
        BigDecimal expected = BigDecimal.ZERO;

        assertEquals(0, expected.compareTo(total), "Debe devolver 0 si no hay BookItem");
    }
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
    @DisplayName("cuenta el numero de asientos reservados por vuelo y cabina")
    public void countReservedSeatsByFlightAndCabin() {
        var flight = createFlight();


        bookItemRepository.save(BookItem.builder()
                .flight(flight)
                .cabin(Cabin.BUSINESS)
                .build());

        bookItemRepository.save(BookItem.builder()
                .flight(flight)
                .cabin(Cabin.BUSINESS)
                .build());

        bookItemRepository.save(BookItem.builder()
                .flight(flight)
                .cabin(Cabin.BUSINESS)
                .build());

        // También agrega uno de otra cabina para verificar que no se cuente
        bookItemRepository.save(BookItem.builder()
                .flight(flight)
                .cabin(Cabin.ECONOMY)
                .build());

        // 3. Llama al método del repositorio para contar
        var reservedSeats = bookItemRepository.countReservedSeatsByFlightAndCabin(
                flight.getId(),
                Cabin.BUSINESS
        );

        // 4. Verifica que el conteo sea el esperado (3)
        assertThat(reservedSeats).isEqualTo(3L);
    }
}
