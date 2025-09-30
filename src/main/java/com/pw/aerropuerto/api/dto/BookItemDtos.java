package com.pw.aerropuerto.api.dto;

import com.pw.aerropuerto.dominio.entities.Flight;

import java.math.BigDecimal;

public class BookItemDtos {
    public record BookItemCreateRequest(String cabinType, BigDecimal price, Long bookingId, Long flightId) {}
    public record BookItemResponse(Long id, Long bookingId, BigDecimal price, Integer segmentOrder, Long flightId) {}
    public record BookItemUpdateRequest(String cabinType, BigDecimal price, Integer segmentOrder) {}
}