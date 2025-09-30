package com.pw.aerropuerto.api.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;

public class FlightDtos {
    public record FlightCreateRequest(String number) implements Serializable {}
    public record FlightUpdateRequest( String number, OffsetDateTime departureTime, OffsetDateTime arrivalTime) implements Serializable{}
    public record FlightResponse(Long id, String number, OffsetDateTime departureTime, OffsetDateTime arrivalTime, Long airlineId, String origin, String destination, Set<TagDtos.TagResponse> tags) implements Serializable {}

}