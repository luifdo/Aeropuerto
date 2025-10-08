package com.pw.aerropuerto.service.mapper;

import com.pw.aerropuerto.api.dto.AirlineDtos;
import com.pw.aerropuerto.dominio.entities.Airline;

import java.util.stream.Collectors;

public class AirlineMapper {

    public static Airline toEntity(AirlineDtos.AirlineCreateRequest request) {
        return Airline.builder().code(request.code()).name(request.name()).build();

    }
    public static AirlineDtos.AirlineResponse ToResponse(Airline airline ) {
        return new AirlineDtos.AirlineResponse(airline.getId(), airline.getName(),
                airline.getCode(), airline.getFlights() == null ? null:
                airline.getFlights().stream().map(FlightMapper::ToResponse).collect(Collectors.toSet()));
    }
    public static void patch(Airline entity, AirlineDtos.AirlineUpdateRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.code() != null) entity.setCode(request.code());
    }
}
