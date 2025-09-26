package com.pw.aerropuerto.service.mapper;

import com.pw.aerropuerto.api.dto.BookingDtos;
import com.pw.aerropuerto.dominio.entities.BookItem;
import com.pw.aerropuerto.dominio.entities.Booking;
import com.pw.aerropuerto.dominio.entities.Passenger;

import java.util.List;

public class BookingMapper {

    public static Booking ToEntity(BookingDtos.BookingCreateRequest request, Passenger passenger, List<BookItem> items) {
        return Booking.builder().createdAt(request.createdAt())
                .passenger(passenger).items(items).build();
    }
    public static BookingDtos.BookingResponse toResponse(Booking entity) {
        return new BookingDtos.BookingResponse(entity.getId(), entity.getCreatedAt(),
                entity.getItems().stream().map(BookItemMapper::ToResponse).toList(),
                entity.getPassenger().getId());
    }
    public static void patch(Booking entity, BookingDtos.BookingUpdateRequest request, Passenger passenger, List<BookItem> items) {
        if (request.updatedAt() != null) entity.setCreatedAt(request.updatedAt());
        if (request.passengerId() != null) entity.setPassenger(passenger);
        if (request.BookItemId() != null && items != null && !items.isEmpty()) entity.setItems(items);
    }
}
