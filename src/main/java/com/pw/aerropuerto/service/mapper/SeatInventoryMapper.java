package com.pw.aerropuerto.service.mapper;

import com.pw.aerropuerto.api.dto.SeatInventoryDtos;
import com.pw.aerropuerto.dominio.entities.Flight;
import com.pw.aerropuerto.dominio.entities.SeatInventory;

public class SeatInventoryMapper {
    public static SeatInventory ToEntity(SeatInventoryDtos.SeatInventoryRequest request ) {
        return SeatInventory.builder().Cabin(request.cabin()).availableSeats(request.availableSeats())
                .totalSeats(request.availableSeats())
                .flight(Flight.builder().id(request.flightId().getId()).build()).build();
    }
    public static SeatInventoryDtos.SeatInventoryResponse ToResponse(SeatInventory seatInventory) {
        return new SeatInventoryDtos.SeatInventoryResponse(
                seatInventory.getId(), seatInventory.getCabin(),
                seatInventory.getTotalSeats(), seatInventory.getAvailableSeats(),
                seatInventory.getFlight() == null ? seatInventory.getFlight().getId() : null
        );
    }

    public static void path(SeatInventory entity, SeatInventoryDtos.SeatInventoryUpdate update) {
        if (update.cabin() != null) entity.setCabin(update.cabin());
        if (update.availableSeats() != null) entity.setAvailableSeats(update.availableSeats());
        if (update.flightId() != 0) entity.setFlight(Flight.builder().id(update.flightId()).build());
    }

}
