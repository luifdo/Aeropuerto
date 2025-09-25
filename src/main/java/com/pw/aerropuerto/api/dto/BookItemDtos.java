package com.pw.aerropuerto.api.dto;

import com.pw.aerropuerto.dominio.entities.Flight;

import java.math.BigDecimal;

public class BookItemDtos {
    public record BookItemCreateRequest(Long cabinId, BigDecimal price, Long bookingId, Long flightId) {}
    public record BookItemResponse(Long id, Long bookingId, BigDecimal price, Integer segmentOrder, Long bookingID, Long flightID ) {}
}
