package com.pw.aerropuerto.service.mapper;

import com.pw.aerropuerto.api.dto.AirportDtos.*;
import com.pw.aerropuerto.api.dto.FlightDtos;
import com.pw.aerropuerto.dominio.entities.Airport;
import com.pw.aerropuerto.dominio.entities.Flight;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = { FlightMapper.class })
public interface AirportMapper {

    AirportMapper INSTANCE = Mappers.getMapper(AirportMapper.class);


    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    Airport toEntity(AirportCreateRequest request);


    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "code", source = "code"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "city", source = "city"),
            @Mapping(target = "flights", expression = "java(mapFlights(airport.getFlights()))"),
            @Mapping(target = "destinations", expression = "java(mapFlights(airport.getDestinations()))")
    })
    AirportResponse toResponse(Airport airport);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(@MappingTarget Airport entity, AirportUpdateRequest request);


    default Set<FlightDtos.FlightResponse> mapFlights(Set<Flight> flights) {
        if (flights == null) {
            return null;
        }
        return flights.stream()
                .map(FlightMapper.INSTANCE::toResponse)
                .collect(Collectors.toSet());
    }
}
