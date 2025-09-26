package com.pw.aerropuerto.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class BookingDtos {
    public record BookingCreateRequest(OffsetDateTime createdAt, Long BookItemId, Long passengerId) implements Serializable {}
    public record BookingUpdateRequest(OffsetDateTime updatedAt, Long BookItemId, Long passengerId) implements Serializable {}
    public record BookingResponse(Long id, OffsetDateTime createdAt, List<BookItemDtos.BookItemResponse> items, Long passengerId) implements Serializable {}

}
