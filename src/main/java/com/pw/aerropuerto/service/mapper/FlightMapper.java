package com.pw.aerropuerto.service.mapper;

import com.pw.aerropuerto.api.dto.FlightDtos;
import com.pw.aerropuerto.api.dto.TagDtos;
import com.pw.aerropuerto.dominio.entities.Flight;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class FlightMapper {
    public static Flight ToEntity(FlightDtos.FlightCreateRequest request) {
        return Flight.builder().number(request.number()).build();
    }
    public static FlightDtos.FlightResponse ToResponse(Flight flight) {
        Set<TagDtos.TagResponse> tagResponses = flight.getTags() == null ? Collections.emptySet() :
                flight.getTags().stream()
                .map(tag -> new TagDtos.TagResponse(tag.getId(), tag.getName(), null))
                .collect(Collectors.toSet());

        return  new FlightDtos.FlightResponse(
                flight.getId(), flight.getNumber(), flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getAirline() != null ? flight.getAirline().getId() : null,
                flight.getOrigin() != null ? flight.getOrigin().getCode() : null,
                flight.getDestination() != null ? flight.getDestination().getCode() : null,
                tagResponses
        );



    }

    public static void path(Flight entity, FlightDtos.FlightUpdateRequest request ) {
        if (request.name() != null ) entity.setNumber(request.name());
        if (request.departureTime() != null ) entity.setDepartureTime(request.departureTime());
        if (request.arrivalTime() != null ) entity.setArrivalTime(request.arrivalTime());
    }
}
