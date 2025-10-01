package com.pw.aerropuerto.service.mapper;

import com.pw.aerropuerto.api.dto.AirlineDtos.*;
import com.pw.aerropuerto.api.dto.FlightDtos;
import com.pw.aerropuerto.dominio.entities.Airline;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {FlightMapper.class})
public interface AirlineMapper {

    AirlineMapper INSTANCE = Mappers.getMapper(AirlineMapper.class);

    Airline toEntity(AirlineCreateRequest request);

    @Mapping(target = "flight", source = "flights")
   AirlineResponse toResponse(Airline airline);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(@MappingTarget Airline entity, AirlineUpdateRequest request);

    default Set<FlightDtos.FlightResponse> mapFlights(Set<com.pw.aerropuerto.dominio.entities.Flight> flights) {
        if (flights == null) {
            return null;
        }
        return flights.stream()
                .map(FlightMapper.INSTANCE::toResponse)
                .collect(java.util.stream.Collectors.toSet());
    }


}
