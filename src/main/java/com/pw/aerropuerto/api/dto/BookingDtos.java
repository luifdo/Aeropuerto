package com.pw.aerropuerto.api.dto;

import com.pw.aerropuerto.dominio.entities.BookItem;
import com.pw.aerropuerto.dominio.entities.Passenger;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class BookingDtos {
    public record BookingCreateRequest(OffsetDateTime createdAt, Long passengerId, List<Long> bookItemIds) implements Serializable {}
    public record BookingUpdateRequest(OffsetDateTime updatedAt, Passenger passengerId, List<BookItem> bookItemIds) implements Serializable {}
    public record BookingResponse(Long id, OffsetDateTime createdAt, List<BookItemDtos.BookItemResponse> items, Long passengerId) implements Serializable {}
}
