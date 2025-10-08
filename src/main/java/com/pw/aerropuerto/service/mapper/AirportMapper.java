package com.pw.aerropuerto.service.mapper;

import com.pw.aerropuerto.api.dto.AirportDtos;
import com.pw.aerropuerto.dominio.entities.Airport;

import java.util.stream.Collectors;

public class AirportMapper {
    public static Airport ToEntity(AirportDtos.AirportCreateRequest request ) {
        return  Airport.builder().Code(request.Code()).Name(request.Name()).build();
    }
    public static  AirportDtos.AirportResponse ToResponse(Airport airport ) {
        return new AirportDtos.AirportResponse(airport.getId(), airport.getCode(),
                airport.getName(), airport.getCity(),
                airport.getFlights() == null ? null :
                airport.getFlights().stream().map(FlightMapper::ToResponse).collect(Collectors.toSet()),
                airport.getDestinations() == null ? null:
                airport.getDestinations().stream().map(FlightMapper::ToResponse).collect(Collectors.toSet()));

    }
    public static void path(Airport entity, AirportDtos.AirportUpdateRequest request ) {
        if (request.Code() != null) entity.setCode(request.Code());
        if (request.Name() != null) entity.setName(request.Name());

    }
}
