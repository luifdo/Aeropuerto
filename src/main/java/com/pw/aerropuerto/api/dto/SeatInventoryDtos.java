package com.pw.aerropuerto.api.dto;

import java.io.Serializable;

public class SeatInventoryDtos {
    public record SeatInventoryRequest(CabinDtos.CabinResponse cabin, Integer availableSeats) implements Serializable {}
    public record SeatInventoryUpdate(CabinDtos.CabinResponse cabin, Integer availableSeats) implements Serializable {}
    public record SeatInventoryResponse(Long id, CabinDtos.CabinResponse cabin, Integer totalSeats, Integer availableSeats, Long flightId) implements Serializable {}
}
