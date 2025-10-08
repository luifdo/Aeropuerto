package com.pw.aerropuerto.service.mapper;


import com.pw.aerropuerto.api.dto.BookItemDtos;
import com.pw.aerropuerto.dominio.entities.BookItem;
import com.pw.aerropuerto.dominio.entities.Booking;
import com.pw.aerropuerto.dominio.entities.Cabin;
import com.pw.aerropuerto.dominio.entities.Flight;

public class BookItemMapper {

    public static BookItem ToEntity(BookItemDtos.BookItemCreateRequest request) {
        String cabinTypeName = request.cabinType();
        Cabin cabinEnum = Cabin.valueOf(cabinTypeName.toUpperCase());

        return BookItem.builder()
                .cabin(cabinEnum)
                .price(request.price())
                .booking(Booking.builder().id(request.bookingId()).build())
                .flight(Flight.builder().id(request.flightId()).build())
                .build();
    }

    public static BookItemDtos.BookItemResponse ToResponse(BookItem bookItem) {
        return new BookItemDtos.BookItemResponse(
                bookItem.getId(),
                bookItem.getCabin() != null ? bookItem.getCabin().toString(): null,
                bookItem.getBooking() != null ? bookItem.getBooking().getId() : null,
                bookItem.getPrice(),
                bookItem.getSegmentOrder(),
                bookItem.getFlight() != null ? bookItem.getFlight().getId() : null
        );
    }

    public static void path(BookItem entity, BookItemDtos.BookItemUpdateRequest request) {
        if (request.cabinType() != null) {
            entity.setCabin(Cabin.valueOf(request.cabinType().toUpperCase()));
        }
        if (request.price() != null) {
            entity.setPrice(request.price());
        }
        if (request.segmentOrder() != null) {
            entity.setSegmentOrder(request.segmentOrder());
        }

    }
}
