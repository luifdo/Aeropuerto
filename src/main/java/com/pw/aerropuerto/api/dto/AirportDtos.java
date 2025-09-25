package com.pw.aerropuerto.api.dto;

import java.io.Serializable;
import java.util.Set;

public class AirportDtos {
    public record AirportCreateRequest(String Code, String Name) implements Serializable {}
    public record AirportUpdateRequest(String Code, String Name) implements Serializable {}
    public record AirportResponse(Long id, String Code, String Name, String City, Set<FlightDtos.FlightResponse> flight, Set<FlightDtos.FlightResponse> destinations) implements Serializable {}
}
