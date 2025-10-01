package com.pw.aerropuerto.service.mapper;

import com.pw.aerropuerto.api.dto.BookingDtos.*;
import com.pw.aerropuerto.dominio.entities.BookItem;
import com.pw.aerropuerto.dominio.entities.Booking;
import com.pw.aerropuerto.dominio.entities.Passenger;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookIteamMapper.class})
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);


    @Mapping(target = "passenger", source = "passenger")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "createdAt", source = "request.createdAt")
    Booking toEntity(BookingCreateRequest request, Passenger passenger, List<BookItem> items);


    @Mapping(target = "items", source = "items")
    @Mapping(target = "passengerId", expression = "java(entity.getPassenger() != null ? entity.getPassenger().getId() : null)")
    BookingResponse toResponse(Booking entity);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "passenger", source = "passenger")
    @Mapping(target = "items", source = "items")
    void patch(@MappingTarget Booking entity, BookingUpdateRequest request, Passenger passenger, List<BookItem> items);
}
