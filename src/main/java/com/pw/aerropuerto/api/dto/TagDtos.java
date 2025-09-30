package com.pw.aerropuerto.api.dto;

import java.io.Serializable;
import java.util.Set;

public class TagDtos {
    public record TagCreateRequest(String name) implements Serializable{}
    public record TagResponse(Long id, String name, Set<FlightDtos.FlightResponse> flight) implements Serializable{}
    public record TagUpdateRequest( String name) implements Serializable{}
}