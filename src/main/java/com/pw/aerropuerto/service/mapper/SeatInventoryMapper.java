package com.pw.aerropuerto.service.mapper;

import com.pw.aerropuerto.api.dto.SeatInventoryDtos;
import com.pw.aerropuerto.dominio.entities.Flight;
import com.pw.aerropuerto.dominio.entities.SeatInventory;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SeatInventoryMapper {

    SeatInventoryMapper INSTANCE = Mappers.getMapper(SeatInventoryMapper.class);

    @Mapping(target = "flight", expression = "java(mapFlightId(request.flightId()))")
    @Mapping(target = "totalSeats", source = "availableSeats")
    SeatInventory toEntity(SeatInventoryDtos.SeatInventoryRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "cabin", source = "cabin")
    @Mapping(target = "totalSeats", source = "totalSeats")
    @Mapping(target = "availableSeats", source = "availableSeats")
    @Mapping(target = "flightId", expression = "java(seatInventory.getFlight() != null ? seatInventory.getFlight().getId() : null)")
    SeatInventoryDtos.SeatInventoryResponse toResponse(SeatInventory seatInventory);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "flight", expression = "java(update.flightId() != 0 ? mapFlightId(update.flightId()) : entity.getFlight())")
    void patch(@MappingTarget SeatInventory entity, SeatInventoryDtos.SeatInventoryUpdate update);


    default Flight mapFlightId(Long flightId) {
        if (flightId == null || flightId == 0L) return null;
        return Flight.builder().id(flightId).build();
    }
}
