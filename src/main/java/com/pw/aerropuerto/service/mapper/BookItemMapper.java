package com.pw.aerropuerto.service.mapper;


import com.pw.aerropuerto.api.dto.BookItemDtos;
import com.pw.aerropuerto.dominio.entities.BookItem;
import com.pw.aerropuerto.dominio.entities.Booking;
import com.pw.aerropuerto.dominio.entities.Cabin;

public class BookItemMapper {

    public static BookItem ToEntity(BookItemDtos.BookItemCreateRequest request ) {
        String cabinTypeName = request.cabinType();
        Cabin cabinEnum = Cabin.valueOf(cabinTypeName.toUpperCase());
        return BookItem.builder().cabin(cabinEnum).price(request.price())
                .booking(new Booking(request.bookingId(),))
                .flight(request.flightId()).build();
    }
}
