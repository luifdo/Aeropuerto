package com.pw.aerropuerto.service.mapper;

import com.pw.aerropuerto.api.dto.FlightDtos;
import com.pw.aerropuerto.api.dto.TagDtos;
import com.pw.aerropuerto.dominio.entities.Tag;

import java.util.Set;
import java.util.stream.Collectors;

public class TagMapper {
    public static Tag ToEntity(TagDtos.TagCreateRequest request){
        return  Tag.builder().name(request.name()).build();
    }
    public static TagDtos.TagResponse toResponse(Tag tag){
        return new TagDtos.TagResponse(
                tag.getId(), tag.getName(),
                tag.getFlights() == null ? Set.of():
                        tag.getFlights().stream().map(flight ->
                                new FlightDtos.FlightResponse(
                                        flight.getId(),flight.getNumber(),
                                        flight.getDepartureTime(),flight.getArrivalTime(),
                                        flight.getAirline() != null ? flight.getAirline().getId() : null,
                                        flight.getOrigin() != null ? flight.getOrigin().getCode() : null,
                                        flight.getDestination() != null ? flight.getDestination().getCode() : null,
                                        null
                                )).collect(Collectors.toSet())
        );
    }
    public static void path(Tag entity,TagDtos.TagCreateRequest request){
        if(entity.getName() != null) entity.setName(entity.getName());
    }
}
