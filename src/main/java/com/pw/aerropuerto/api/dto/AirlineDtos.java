package com.pw.aerropuerto.api.dto;

import java.io.Serializable;
import java.util.Set;

public class AirlineDtos {
    public record AirlineCreateRequest(String name, String code) implements Serializable {}
    public record AirlineResponse(Long id, String name, String code, Set<FlightDtos.FlightResponse> flight) implements Serializable {}
    public record AirlineUpdateRequest(String name, String code
    ) implements Serializable {}
}