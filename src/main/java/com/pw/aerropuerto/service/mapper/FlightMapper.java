package com.pw.aerropuerto.service.mapper;

import com.pw.aerropuerto.api.dto.FlightDtos.*;
import com.pw.aerropuerto.api.dto.TagDtos;
import com.pw.aerropuerto.dominio.entities.Flight;
import com.pw.aerropuerto.dominio.entities.Tag;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface FlightMapper {

    FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);

    // Mapear FlightCreateRequest a entidad Flight
    @Mapping(target = "number", source = "number")
    Flight toEntity(FlightCreateRequest request);

    // Mapear entidad Flight a FlightResponse DTO
    @Mapping(target = "id", source = "id")
    @Mapping(target = "number", source = "number")
    @Mapping(target = "departureTime", source = "departureTime")
    @Mapping(target = "arrivalTime", source = "arrivalTime")
    @Mapping(target = "airlineId", expression = "java(flight.getAirline() != null ? flight.getAirline().getId() : null)")
    @Mapping(target = "originCode", expression = "java(flight.getOrigin() != null ? flight.getOrigin().getCode() : null)")
    @Mapping(target = "destinationCode", expression = "java(flight.getDestination() != null ? flight.getDestination().getCode() : null)")
    @Mapping(target = "tags", expression = "java(mapTags(flight.getTags()))")
   FlightResponse toResponse(Flight flight);

    // Patch: actualizar parcialmente Flight a partir de FlightUpdateRequest
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "number", source = "name")  // Corrigiendo que el campo sea number, viene del request.name
    void patch(@MappingTarget Flight entity, FlightUpdateRequest request);

    // Método auxiliar para mapear tags
    default Set<TagDtos.TagResponse> mapTags(Set<Tag> tags) {
        if (tags == null) {
            return Collections.emptySet();
        }
        return tags.stream()
                .map(tag -> new TagDtos.TagResponse(tag.getId(), tag.getName(), null))
                .collect(Collectors.toSet());
    }
}
