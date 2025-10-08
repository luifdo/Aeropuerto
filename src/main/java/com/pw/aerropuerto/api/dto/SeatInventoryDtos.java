package com.pw.aerropuerto.api.dto;

import com.pw.aerropuerto.dominio.entities.Cabin;
import com.pw.aerropuerto.dominio.entities.Flight;

import java.io.Serializable;

public class SeatInventoryDtos {
    public record SeatInventoryRequest(Cabin cabin, Integer availableSeats, Flight flightId) implements Serializable {}
    public record SeatInventoryUpdate(Cabin cabin, Integer availableSeats, Long flightId) implements Serializable {}
    public record SeatInventoryResponse(Long id, Cabin cabin, Integer totalSeats, Integer availableSeats, Long flightId) implements Serializable {}
}
